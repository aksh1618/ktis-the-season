import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = Pair(first + pair.first, second + pair.second)

/**
 * [Double.pow] counterpart for Int, uses double conversion internally
 */
fun Int.pow(exponent: Int) = toDouble().pow(exponent).toInt()

/**
 * Returns masked complement, i.e. expected value in the [maskBits] bits instead of a negative number because of
 * 2's complement representation
 *
 * For example, for 9:
 * ```
 *  Binary representation:         00...01001
 *  Complement:                    11...10110 which is -10
 *  Masking with 4 bits:  and with 00...01111
 *  So we get:                     00...00110 which is the expected bitwise complement
 * ```
 * @param maskBits Number of bits to mask the complement to
 * @return Decimal representation of the masked complement
 */
fun Int.inv(maskBits: Int) = inv() and 2.pow(maskBits) - 1
