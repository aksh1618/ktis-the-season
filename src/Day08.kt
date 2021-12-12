/**
 * ```
 *    1111
 *   2    3
 *   2    3
 *    4444
 *   5    6
 *   5    6
 *    7777
 * ```
 */
val SEGMENTS_FOR_NUMBER = mapOf(
    0 to setOf(1, 2, 3, 5, 6, 7),       // 6
    1 to setOf(3, 6),                   // 2: unique
    2 to setOf(1, 3, 4, 5, 7),          // 5
    3 to setOf(1, 3, 4, 6, 7),          // 5
    4 to setOf(2, 3, 4, 6),             // 4: unique
    5 to setOf(1, 2, 4, 6, 7),          // 5
    6 to setOf(1, 2, 4, 5, 6, 7),       // 6
    7 to setOf(1, 3, 6),                // 3: unique
    8 to setOf(1, 2, 3, 4, 5, 6, 7),    // 7: unique
    9 to setOf(1, 2, 3, 4, 6, 7),       // 6
)

data class Signal(val letters: Set<Char>)
typealias SignalPatterns = List<Signal>
typealias OutputDigits = List<Signal>

val Signal.possibleDigits
    get() = SEGMENTS_FOR_NUMBER
        .filter { (_, segments) -> segments.size == letters.size }
        .keys

fun main() {
    fun readSignalAndDigits(input: List<String>): List<Pair<SignalPatterns, OutputDigits>> {
        return input.map { line ->
            val (a, b) = line.split(" | ")
            a.split(" ").map { Signal(it.toSet()) } to b.split(" ").map { Signal(it.toSet()) }
        }
    }

    fun part1(input: List<Pair<SignalPatterns, OutputDigits>>): Int {
        return input.sumOf { (_, outputDigits) -> outputDigits.count { it.possibleDigits.size == 1 } }
    }

    fun deduceOutputForSignals(pattern: SignalPatterns, output: OutputDigits): Int {
        // The letter associated with the segment number
        val letterForSegment = mutableMapOf<Int, Char>()
        // The set of letters associated with a number: already contains 1, 4, 7, 8
        val lettersForNumber = pattern
            .map { it.possibleDigits to it.letters }
            .filter { it.first.size == 1 }.toMap()
            .mapKeys { (k, _) -> k.single() }
            .toMutableMap()
        val either2or3or5 = pattern.map { it.letters }.filter { it.size == 5 }
        val either0or6or9 = pattern.map { it.letters }.filter { it.size == 6 }
        letterForSegment[1] = (lettersForNumber[7]!! - lettersForNumber[1]!!).single()
        lettersForNumber[9] = either0or6or9.single { (it - (lettersForNumber[4]!! + lettersForNumber[7]!!)).size == 1 }
        letterForSegment[7] = (lettersForNumber[9]!! - (lettersForNumber[4]!! + lettersForNumber[7]!!)).single()
        lettersForNumber[3] = either2or3or5.single { (it - (lettersForNumber[7]!! + letterForSegment[7]!!)).size == 1 }
        letterForSegment[4] = (lettersForNumber[3]!! - (lettersForNumber[7]!! + letterForSegment[7]!!)).single()
        lettersForNumber[0] = either0or6or9.single { it == (lettersForNumber[8]!! - letterForSegment[4]!!) }
        lettersForNumber[6] = either0or6or9.single { (it != lettersForNumber[9]) && (it != lettersForNumber[0]) }
        letterForSegment[5] = (lettersForNumber[6]!! - lettersForNumber[9]!!).single()
        lettersForNumber[2] = either2or3or5.single { letterForSegment[5]!! in it }
        lettersForNumber[5] = either2or3or5.single { (it != lettersForNumber[2]) && (it != lettersForNumber[3]) }
        val numbersMap = lettersForNumber.map { (k, v) -> v to k.toString() }.toMap()
        return output.joinToString(separator = "") { numbersMap[it.letters]!! }.toInt()
    }

    fun part2(input: List<Pair<SignalPatterns, OutputDigits>>): Int {
        return input.sumOf { deduceOutputForSignals(it.first, it.second) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readSignalAndDigits(readInput("Day08_test"))
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readSignalAndDigits(readInput("Day08"))
    println(part1(input))
    println(part2(input))
}

