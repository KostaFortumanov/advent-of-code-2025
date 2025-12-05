import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

private fun readFileMatrix(day: Int): List<List<String>> =
    readFileLines(day).map { line -> line.split("").filter { it.isNotEmpty() } }

fun readFileCharMatrix(day: Int): List<List<Char>> =
    readFileMatrix(day).map { line -> line.map { it.first() } }

fun <T> List<List<T>>.countGridEntries(predicate: (T) -> Boolean) = sumOf { it.count(predicate) }

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    fold(listOf(emptyList())) { acc, element ->
        if (predicate(element)) {
            acc + listOf(emptyList())
        } else {
            acc.dropLast(1) + listOf(acc.last() + element)
        }
    }

fun Any?.println() = println(this)
