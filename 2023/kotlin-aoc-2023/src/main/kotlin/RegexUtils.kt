import java.lang.IllegalArgumentException

object RegexUtils {
    fun String.parseUsingRegex(regex: Regex) =
        regex.find(this)
            ?.destructured
            ?: throw IllegalArgumentException(this)

    fun String.parseUsingRegex(regex: String) = this.parseUsingRegex(regex.toRegex())
}