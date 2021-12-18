typealias Dot = Pair<Int, Int>

enum class FoldDirection { UP, LEFT }
data class FoldInstruction(val direction: FoldDirection, val value: Int)

fun main() {
    fun readDotsAndInstructions(input: List<String>): Pair<List<Dot>, List<FoldInstruction>> {
        return input.takeWhile { it.isNotBlank() }
            .map { it.split(",").let { (x, y) -> x.toInt() to y.toInt() } } to input.takeLastWhile { it.isNotBlank() }
            .map { FoldInstruction(if ('x' in it) FoldDirection.LEFT else FoldDirection.UP, it.split("=")[1].toInt()) }
    }

    fun applyFoldInstruction(dots: List<Dot>, instruction: FoldInstruction): List<Dot> {
        return dots.map { (x, y) ->
            instruction.let { (direction, value) ->
                when (direction) {
                    FoldDirection.LEFT -> if (x > value) Dot(x - (2 * (x - value)), y) else x to y
                    FoldDirection.UP -> if (y > value) Dot(x, y - (2 * (y - value))) else x to y
                }
            }
        }.distinct()
    }

    fun part1(dots: List<Dot>, instructions: List<FoldInstruction>): Int {
        return applyFoldInstruction(dots, instructions.first()).size
    }

    fun part2(dots: List<Dot>, instructions: List<FoldInstruction>): String {
        val dotsAfterFolding = instructions.fold(dots, ::applyFoldInstruction)
        return buildString {
            (0..dotsAfterFolding.maxOf { it.second }).map { y ->
                (0..dotsAfterFolding.maxOf { it.first }).map { x ->
                    if (x to y in dotsAfterFolding) append("# ") else append(". ")
                }
                append("\n")
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readDotsAndInstructions(readInput("Day13_test"))
    check(part1(testInput.first, testInput.second) == 17)

    val input = readDotsAndInstructions(readInput("Day13"))
    println(part1(input.first, input.second))
    println(part2(input.first, input.second))
}
