data class Cave(val name: String) {
    fun isSmall() = name.all { it.isLowerCase() }
    val neighbours = mutableListOf<Cave>()
}
typealias Caves = MutableMap<String, Cave>

fun main() {
    fun readCaves(input: List<String>): MutableMap<String, Cave> {
        return mutableMapOf<String, Cave>().apply {
            input.map {
                val (cave1, cave2) = it.split("-")
                computeIfAbsent(cave1) { Cave(it) }.neighbours += computeIfAbsent(cave2) { Cave(it) }
                get(cave2)!!.neighbours += get(cave1)!!
            }
        }
    }

    fun countPaths(start: Cave, end: Cave, cantVisit: Set<Cave>): Int {
        return start.neighbours.filter { it !in cantVisit }.sumOf {
            if (it == end) 1 else countPaths(it, end, cantVisit + if (it.isSmall()) setOf(it) else emptySet())
        }
    }

    fun countPaths2(
        start: Cave,
        end: Cave,
        cantVisit: Set<Cave>,
        visitedTwice: Boolean,
        pathSoFar: String,
        allPaths: MutableSet<String>,
    ): Int {
        return start.neighbours.filter { it !in cantVisit }.sumOf {
            if (it == end) {
                val finalPath = "$pathSoFar,end"
                if (finalPath !in allPaths) {
                    allPaths += finalPath
                    1
                } else 0
            } else if (it.isSmall()) {
                if (visitedTwice) {
                    // Some small cave already visited twice
                    countPaths2(it, end, cantVisit + it, true, "$pathSoFar,${it.name}", allPaths)
                } else {
                    // These two cases can lead to the same path in some cases, so need to collect paths in a set
                    // Visit this small cave again
                    countPaths2(it, end, cantVisit, true, "$pathSoFar,${it.name}", allPaths) +
                            // Visit some upcoming small cave again
                            countPaths2(it, end, cantVisit + it, false, "$pathSoFar,${it.name}", allPaths)
                }
            } else {
                countPaths2(it, end, cantVisit, visitedTwice, "$pathSoFar,${it.name}", allPaths)
            }
        }
    }

    fun part1(input: Caves): Int {
        return countPaths(input["start"]!!, input["end"]!!, setOf(input["start"]!!))
    }

    fun part2(input: Caves): Int {
        return countPaths2(input["start"]!!, input["end"]!!, setOf(input["start"]!!), false, "start", mutableSetOf())
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readCaves(readInput("Day12_test"))
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)
    val testInput2 = readCaves(readInput("Day12_test2"))
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)
    val testInput3 = readCaves(readInput("Day12_test3"))
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3509)

    val input = readCaves(readInput("Day12"))
    println(part1(input))
    println(part2(input))
}
