fun main() {

    fun part1String(input: List<String>): Int {
        return input
            .fold(MutableList(input[0].length) { 0 }) { list, current ->
                list.apply {
                    current.forEachIndexed { i, bit -> set(i, get(i) + bit.digitToInt()) }
                }
            } // [number of 1's in ith bit]
            .map { if (it > input.size / 2) 1 else 0 } // [most common bit]
            .joinToString("") // gamma rate in binary string
            .run {
                toInt(2) * map { c -> if (c == '0') '1' else '0' }.joinToString("").toInt(2)
            } // decimal gamma rate * binary gamma rate complemented bitwise to get decimal epsilon rate
    }

    fun part1Bits(input: List<String>): Int {
        val noOfBits = input[0].length
        return input
            .fold(MutableList(noOfBits) { 0 }) { list, current ->
                list.apply {
                    current.forEachIndexed { i, bit -> set(i, get(i) + bit.digitToInt()) }
                }
            } // [number of 1's in ith bit]
            .map { if (it > input.size / 2) 1 else 0 } // [most common bit]
            .joinToString("") // gamma rate in binary
            .toInt(2) // gamma rate in decimal
            .run { this * inv(maskBits = noOfBits) } // times epsilon rate (Masked to overcome 2's complement negation)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1String(testInput) == 198)
    check(part1Bits(testInput) == 198)

    val input = readInput("Day03")
    println(part1String(input))
    println(part1Bits(input))
    println(part2(input))
}
