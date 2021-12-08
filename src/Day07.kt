import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

private fun Double.ints() = listOf(floor(this).toInt(), ceil(this).toInt())
private fun List<Int>.fuelSpentForPosition(e: Int) = sumOf { abs(it - e) }
private fun List<Int>.incrementalFuelSpentForPosition(e: Int) = sumOf { ((abs(it - e)) * (abs(it - e) + 1)) / 2 }
private fun List<Int>.mean() = sumOf { it } / size.toDouble()
private fun List<Int>.median() = when (size % 2) {
    0 -> sorted().let { (it[size / 2 - 1] + it[size / 2]) / 2.0 }
    else -> sorted()[size / 2].toDouble()
}

fun main() {
    fun part1(input: List<Int>): Int {
        return input.median().ints().minOf { input.fuelSpentForPosition(it) }
    }

    fun part2(input: List<Int>): Int {
        return input.mean().ints().minOf { input.incrementalFuelSpentForPosition(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test").first().split(",").map { it.toInt() }
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07").first().split(",").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
