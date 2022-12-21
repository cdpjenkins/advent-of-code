object StringUtils {
    fun indentBy(indent: Int) = (1..indent).map { " " }.joinToString("")
}