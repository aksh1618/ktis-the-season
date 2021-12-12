import java.util.*

val CLOSING_CHAR_TO_OPENING_CHAR = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<',
)
val OPENING_CHAR_TO_CLOSING_CHAR = CLOSING_CHAR_TO_OPENING_CHAR.inverse()
val ILLEGAL_CHAR_POINTS = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)
val CLOSING_CHAR_POINTS = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

fun main() {

    fun getCorruptedLines(input: List<String>) = input.mapIndexedNotNull { i, chars ->
        Stack<Char>().apply {
            chars.forEach {
                if (it !in CLOSING_CHAR_TO_OPENING_CHAR) push(it)
                else if (pop() != CLOSING_CHAR_TO_OPENING_CHAR[it]) return@mapIndexedNotNull i to it
            }
        }
        null
    }

    fun part1(input: List<String>): Int {
        return getCorruptedLines(input).sumOf { ILLEGAL_CHAR_POINTS[it.second]!! }
    }

    fun part2(input: List<String>): Long {
        val corruptedLineIndices = getCorruptedLines(input).map { it.first }
        val nonCorruptedLines = input.indices.filter { it !in corruptedLineIndices }.map { input[it] }
        return nonCorruptedLines.map { chars ->
            Stack<Char>().apply {
                chars.forEach { if (it !in CLOSING_CHAR_TO_OPENING_CHAR) push(it) else pop() }
            }.map { OPENING_CHAR_TO_CLOSING_CHAR[it]!! }.map { CLOSING_CHAR_POINTS[it]!! }.foldRight(0L) { i, acc ->
                (acc * 5) + i
            }
        }.sorted()[nonCorruptedLines.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
