typealias Board = List<List<Int>>

fun main() {

    fun readNumbersAndBoards(input: List<String>): Pair<List<Int>, List<Board>> {
        return input[0].split(",").map { it.toInt() } to
                input
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .windowed(5, 5)
                    .map { boardRows ->
                        boardRows.map { row -> row.trim().split("  ", " ").map { it.toInt() } }
                    }
    }

    fun Board.checkWinning(numbersDrawn: Set<Int>): Boolean {
        return (0 until 5).any { i ->
            (0 until 5).all { j -> this[i][j] in numbersDrawn }
                    || (0 until 5).all { j -> this[j][i] in numbersDrawn }
        }
    }

    fun part1(numbersToDraw: List<Int>, boards: List<Board>): Int {
        var winningBoardIndex = 0
        var lastNumberDrawnIndex = 0
        var low = 0
        var high = numbersToDraw.size - 1
        // Binary search for first drawn number with a winning board
        while (low <= high) {
            val mid = (low + high).ushr(1) // safe from overflows
            val numbersDrawn = numbersToDraw.subList(0, mid + 1).toSet()
            val anyWinningBoardIndex = boards.indices.firstOrNull { boards[it].checkWinning(numbersDrawn) }
            if (anyWinningBoardIndex == null) {
                // No winning board: Draw more numbers
                low = mid + 1
            } else {
                // One or more winning boards: Draw lesser numbers to see if any winning boards
                winningBoardIndex = anyWinningBoardIndex
                lastNumberDrawnIndex = mid
                high = mid - 1
            }
        }
        val numbersDrawn = numbersToDraw.subList(0, lastNumberDrawnIndex + 1).toSet()
        val lastNumberDrawn = numbersToDraw[lastNumberDrawnIndex]
        return lastNumberDrawn * boards[winningBoardIndex].sumOf { row -> row.filter { it !in numbersDrawn }.sum() }
    }

    fun part2(numbersToDraw: List<Int>, boards: List<Board>): Int {
        return numbersToDraw.size
    }

    // test if implementation meets criteria from the description, like:
    val (testNumbersToDraw, testBoards) = readNumbersAndBoards(readInput("Day04_test"))
    check(part1(testNumbersToDraw, testBoards) == 4512)

    val (numbersToDraw, boards) = readNumbersAndBoards(readInput("Day04"))
    println(part1(numbersToDraw, boards))
    println(part2(numbersToDraw, boards))
}
