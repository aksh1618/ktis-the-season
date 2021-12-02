fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map {
                when {
                    it.startsWith("down") -> Pair(0, it.split(" ")[1].toInt())
                    it.startsWith("up") -> Pair(0, it.split(" ")[1].toInt() * -1)
                    else -> Pair(it.split(" ")[1].toInt(), 0)
                }
            }
            .reduce { acc, pair -> acc + pair }
            .run { first * second }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
