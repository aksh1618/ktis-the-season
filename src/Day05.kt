import java.lang.Integer.max
import java.lang.Integer.min

typealias Point = Pair<Int, Int>
typealias LineSegment = Pair<Point, Point>

fun LineSegment.points(): List<Point> {
    val (p1, p2) = this
    val (x1, y1) = p1
    val (x2, y2) = p2
    val points = mutableListOf<Point>()
    if (x1 == x2) (min(y1, y2)..max(y1, y2)).forEach { points += x1 to it }
    else if (y1 == y2) (min(x1, x2)..max(x1, x2)).forEach { points += it to y1 }
    else {
        points += x1 to y1
        var i = x1
        var j = y1
        while (i != x2 && j != y2) {
            if (i > x2) i--
            else if (i < x2) i++
            if (j > y2) j--
            else if (j < y2) j++
            points += i to j
        }
    }
    return points
}

fun main() {

    fun readLineSegments(input: List<String>): List<LineSegment> {
        return input.map { line ->
            line.split(" -> ").map { coordinates ->
                coordinates.split(",").map { it.toInt() }.zipWithNext().single()
            }.zipWithNext().single()
        }
    }

    fun intersectionsForLinesSatisfying(
        lineSegments: List<LineSegment>,
        predicate: (LineSegment) -> Boolean,
    ): Int {
        return lineSegments
            .filter(predicate)
            .fold(mutableMapOf<Point, Int>()) { map, lineSegment ->
                map.apply {
                    lineSegment.points().forEach { put(it, (getOrPut(it) { 0 }) + 1) }
                }
            }
            .count { it.value >= 2 }
    }

    fun part1(lineSegments: List<LineSegment>): Int {
        return intersectionsForLinesSatisfying(lineSegments) { (point1, point2) ->
            point1.first == point2.first || point1.second == point2.second
        }
    }

    fun part2(lineSegments: List<LineSegment>): Int {
        return intersectionsForLinesSatisfying(lineSegments) { true }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLineSegments(readInput("Day05_test"))
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readLineSegments(readInput("Day05"))
    println(part1(input))
    println(part2(input))
}
