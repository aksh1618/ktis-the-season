typealias InsertionRules = Map<String, String>

fun main() {
    fun readRules(input: List<String>): InsertionRules {
        return input.drop(2).associate { it.split(" -> ").let { (elements, element) -> elements to element } }
    }

    fun applyInsertionRulesNaive(template: String, rules: InsertionRules): String {
        return template.windowed(2).joinToString("") { pair -> pair[0] + (rules[pair] ?: "") } + template.last()
    }

    fun part1(template: String, rules: InsertionRules): Int {
        val polymer = (1..10).fold(template) { template, _ -> applyInsertionRulesNaive(template, rules) }
        val elementCounts = polymer.groupingBy { it }.eachCount()
        val mostCommonQuantity = elementCounts.maxOf { it.value }
        val leastCommonQuantity = elementCounts.minOf { it.value }
        return mostCommonQuantity - leastCommonQuantity
    }

    fun applyInsertionRulesSmart(templatePairOccurrences: Map<String, Long>, rules: InsertionRules): Map<String, Long> {
        val newTemplatePairOccurrences = mutableMapOf<String, Long>()
        templatePairOccurrences.forEach { (pair, count) ->
            // If rule exists, insert new pairs, otherwise insert the same pair
            if (pair in rules) rules[pair]!!.let {
                newTemplatePairOccurrences[pair[0] + it] = (newTemplatePairOccurrences[pair[0] + it] ?: 0) + count
                newTemplatePairOccurrences[it + pair[1]] = (newTemplatePairOccurrences[it + pair[1]] ?: 0) + count
            } else {
                newTemplatePairOccurrences[pair] = (newTemplatePairOccurrences[pair] ?: 0) + count
            }
        }
        return newTemplatePairOccurrences
    }

    fun part2(template: String, rules: InsertionRules): Long {
        val initialCounts =
            template.windowed(2).groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }
        // Frequency map of element pairs
        val polymerPairCounts = (1..40).fold(initialCounts) { templateCounts, i ->
            applyInsertionRulesSmart(templateCounts, rules)
        }
        // Split to form frequency map of elements
        val elementCounts = polymerPairCounts.flatMap { (pair, count) -> listOf(pair[0] to count, pair[1] to count) }
            .groupBy({ it.first }, { it.second })
            // First and last element are not double counted one time each, adjusting for that
            .map { if (it.key == template.first() || it.key == template.last()) it.value.sum() + 1 else it.value.sum() }
        val mostCommonQuantity = elementCounts.maxOf { it }
        val leastCommonQuantity = elementCounts.minOf { it }
        // Every element is counted twice
        return (mostCommonQuantity - leastCommonQuantity) / 2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput.first(), readRules(testInput)) == 1588)
    check(part2(testInput.first(), readRules(testInput)) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input.first(), readRules(input)))
    println(part2(input.first(), readRules(input)))
}
