typealias Heightmap = List<List<Int>>

val Heightmap.rows get() = size
val Heightmap.cols get() = this[0].size
operator fun Heightmap.get(coords: Pair<Int, Int>) = this[coords.first][coords.second]
fun Heightmap.getNeighbourCoordinates(i: Int, j: Int) = listOf(
    i - 1 to j,
    i + 1 to j,
    i to j - 1,
    i to j + 1,
).filter { (i, j) -> i != -1 && j != -1 && i != rows && j != cols }

fun Heightmap.getLowPoints() = this.flatMapIndexed { i, row ->
    row.indices.filter { j -> getNeighbourCoordinates(i, j).map { this[it] }.all { this[i][j] < it } }.map { i to it }
}

fun Heightmap.getBasinSize(coords: Pair<Int, Int>): Int {
    var count = 1
    val neighboursToVisit = ArrayDeque(listOf(coords))
    val visitedCoords = mutableSetOf(coords)
    while (neighboursToVisit.isNotEmpty()) {
        neighboursToVisit.removeFirst().let { (i, j) ->
            getNeighbourCoordinates(i, j).filter { this[it] < 9 }.filter { it !in visitedCoords }.forEach {
                neighboursToVisit += it
                visitedCoords += it
                count += 1
            }
        }
    }
    return count
}

fun main() {
    fun readHeightmap(input: List<String>): Heightmap = input.map { it.map { it.digitToInt() } }

    fun part1(input: Heightmap): Int {
        return input.getLowPoints().sumOf { input[it] + 1 }
    }

    fun part2(input: Heightmap): Int {
        return input.getLowPoints().map { input.getBasinSize(it) }.sortedDescending().take(3).reduce { p, i -> p * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readHeightmap(readInput("Day09_test"))
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readHeightmap(readInput("Day09"))
    println(part1(input))
    println(part2(input))
}
