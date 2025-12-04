import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

private fun readFileMatrix(day: Int): List<List<String>> =
    readFileLines(day).map { line -> line.split("").filter { it.isNotEmpty() } }

fun readFileCharMatrix(day: Int): List<List<Char>> =
    readFileMatrix(day).map { line -> line.map { it.first() } }

fun <T> List<List<T>>.countGridEntries(predicate: (T) -> Boolean) = sumOf { it.count(predicate) }

fun Any?.println() = println(this)
