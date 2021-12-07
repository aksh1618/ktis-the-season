fun main() {

    fun simulateForDays(input: List<Int>, days: Int): Long {
        return (0 until days).fold(
            input.groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        ) { map, _ ->
            val newMap = mutableMapOf<Int, Long>()
            map.forEach { (key, _) ->
                when (key) {
                    0 -> {
                        newMap[6] = (newMap[6] ?: 0) + (map[0] ?: 0)
                        newMap[8] = (newMap[8] ?: 0) + (map[0] ?: 0)
                    }
                    else -> {
                        newMap[key - 1] = (newMap[key - 1] ?: 0) + (map[key] ?: 0)
                    }
                }
            }
            newMap
        }.values.sum()
    }

    fun part1(input: List<Int>): Long {
        return simulateForDays(input, 80)
    }

    fun part2(input: List<Int>): Long {
        return simulateForDays(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test").first().split(",").map { it.toInt() }
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06").first().split(",").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
