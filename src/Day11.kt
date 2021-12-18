open class MutableGrid<T>(val values: List<MutableList<T>>) {
    val rows = values.size
    val cols = values[0].size
    val indices = (0 until rows).flatMap { i -> (0 until cols).map { j -> i to j } }
    operator fun get(coords: Pair<Int, Int>) = values[coords.first][coords.second]
    operator fun set(coords: Pair<Int, Int>, value: T) {
        values[coords.first][coords.second] = value
    }

    fun getNeighbourCoordinates(coords: Pair<Int, Int>) = coords.let { (i, j) ->
        listOf(i, i - 1, i + 1).flatMap { i -> listOf(j, j - 1, j + 1).map { j -> i to j } }
            .filter { (i, j) -> i != -1 && j != -1 && i != rows && j != cols && !(i == coords.first && j == coords.second) }
    }

    override fun toString() = buildString {
        values.forEach { row -> row.forEach { append("$it ") }; append("\n") }
    }
}

class OctopusGrid(energyLevels: List<MutableList<Int>>) : MutableGrid<Int>(energyLevels) {
    val FLASH_POINT = 10
    fun simulateStepForFlashes(): Int {
        var flashes = 0
        // Everyone gains one energy level
        indices.forEach { this[it] += 1 }
        // Let the cascade begin!
        while (indices.any { this[it] >= FLASH_POINT }) {
            indices.forEach {
                if (this[it] >= FLASH_POINT) {
                    this[it] = 0
                    getNeighbourCoordinates(it).forEach {
                        if (this[it] != 0) this[it] += 1
                    }
                }
            }
        }
        // Light show!
        flashes += indices.count { this[it] == 0 }
        return flashes
    }
}

fun main() {

    fun readOctopusGrid(input: List<String>): OctopusGrid =
        OctopusGrid(input.map { it.map { it.digitToInt() }.toMutableList() })

    fun part1(input: OctopusGrid): Int {
        return (1..100).sumOf { input.simulateStepForFlashes() }
    }

    fun part2(input: OctopusGrid): Int {
        return (1..Int.MAX_VALUE).first { input.simulateStepForFlashes() == input.indices.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(readOctopusGrid(testInput)) == 1656)
    check(part2(readOctopusGrid(testInput)) == 195)

    val input = readInput("Day11")
    println(part1(readOctopusGrid(input)))
    println(part2(readOctopusGrid(input)))
}
