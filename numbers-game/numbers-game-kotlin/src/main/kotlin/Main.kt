import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    val target = 156

    val elapsed = measureTimeMillis {
        val possibleExpressions =
            solve(
                numbers(2, 3, 7, 11, 15, 25),
                target
            )

        possibleExpressions
            .filter { it.value == target }
            .forEach {
                println("${it.value} $it")
            }
    }

    println("Time taken: ${elapsed}ms")
}

private fun numbers(vararg nums: Int) = nums.map { NumberExpression(it) }

fun solve(numbers: List<Expression>, target: Int): List<Expression> {
    if (numbers.size == 0) {
        return emptyList()
    }

    val results = numbers.filter { it.value == target }
    if (results.isNotEmpty()) {
        return results
    }

    val possibleExpressions = numbers.flatMap { lhs ->
        numbers.minus(lhs).flatMap { rhs ->
            listOf(Operator.PLUS, Operator.MINUS, Operator.MULTIPLY, Operator.DIVIDE).flatMap { operator ->
                val newExpression = OperatorExpression(lhs, operator, rhs)

                if (newExpression.valid) {
                    val newNumbers = numbers.minus(lhs).minus(rhs).plus(newExpression)

                    listOf(solve(newNumbers, target))
                } else {
                    listOf()
                }
            }
        }
    }.flatten()

    return possibleExpressions
}

enum class Operator(val symbol: String) {
    PLUS("+") {
        override fun apply(lhs: Int, rhs: Int) = lhs + rhs
        override fun isValid(lhs: Expression, rhs: Expression) = true
    },
    MINUS("-") {
        override fun apply(lhs: Int, rhs: Int) = lhs - rhs
        override fun isValid(lhs: Expression, rhs: Expression) = apply(lhs.value, rhs.value) > 0
    },
    MULTIPLY("*") {
        override fun apply(lhs: Int, rhs: Int) = lhs * rhs
        override fun isValid(lhs: Expression, rhs: Expression) = true
    },
    DIVIDE("/") {
        override fun apply(lhs: Int, rhs: Int) = lhs / rhs
        override fun isValid(lhs: Expression, rhs: Expression) = lhs.value.mod(rhs.value) == 0
    };

    abstract fun apply(lhs: Int, rhs: Int): Int
    abstract fun isValid(lhs: Expression, rhs: Expression): Boolean

    override fun toString(): String {
        return this.symbol
    }

}

sealed interface Expression {
    val value: Int
    val valid: Boolean
}
data class NumberExpression(val num: Int) : Expression {
    override val value: Int = num
    override val valid: Boolean = true

    override fun toString(): String {
        return num.toString()
    }
}
data class OperatorExpression(val lhs: Expression, val operator: Operator, val rhs: Expression): Expression {
    override val value: Int = operator.apply(lhs.value, rhs.value)
    override val valid: Boolean = operator.isValid(lhs, rhs)

    override fun toString(): String {
        return "($lhs $operator $rhs)"
    }
}
