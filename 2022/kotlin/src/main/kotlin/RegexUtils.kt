import java.lang.IllegalArgumentException

object RegexUtils {
    fun String.parseUsingRegex(regex: String) =
        (regex.toRegex()
            .find(this)
            ?.destructured
            ?: throw IllegalArgumentException(this))
}