package day18

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput
import java.lang.AssertionError

class Day18Test : FunSpec({

    test("part 1 with real data: operators have equal precedence") {
        val result = realInput("day18")
            .map { evaluateWhereOperatorsHaveEqualPrecedence(it) }
            .sum()
        result shouldBe 53660285675207L
    }

    test("part 2 with real data: plus has greater precedence than multiply") {
        val result = realInput("day18")
            .map { evaluateWherePlusHasGreaterPrecedenceThanMultiply(it) }
            .sum()
        result shouldBe 141993988282687L
    }


    test("evaluates infix expression with just operations and no brackets") {
        evaluateWhereOperatorsHaveEqualPrecedence("1 + 2 * 3 + 4 * 5 + 6") shouldBe 71
    }

    test("evaluates infix expression with brackets") {
        evaluateWhereOperatorsHaveEqualPrecedence("1 + (2 * 3) + (4 * (5 + 6))") shouldBe 51
    }

    test("moar examples") {
        evaluateWhereOperatorsHaveEqualPrecedence("2 * 3 + (4 * 5)") shouldBe 26
        evaluateWhereOperatorsHaveEqualPrecedence("5 + (8 * 3 + 9 + 3 * 4 * 3)") shouldBe 437
        evaluateWhereOperatorsHaveEqualPrecedence("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") shouldBe 12240
        evaluateWhereOperatorsHaveEqualPrecedence("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") shouldBe 13632
    }

    test("evalutes moar infix exprewssions where plus has greater precedence than multiply") {
        evaluateWherePlusHasGreaterPrecedenceThanMultiply("1 + (2 * 3) + (4 * (5 + 6))") shouldBe 51
        evaluateWherePlusHasGreaterPrecedenceThanMultiply("2 * 3 + (4 * 5)") shouldBe 46
        evaluateWherePlusHasGreaterPrecedenceThanMultiply("5 + (8 * 3 + 9 + 3 * 4 * 3)") shouldBe 1445
        evaluateWherePlusHasGreaterPrecedenceThanMultiply("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") shouldBe 669060
        evaluateWherePlusHasGreaterPrecedenceThanMultiply("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") shouldBe 23340
    }
})

private fun evaluateWhereOperatorsHaveEqualPrecedence(input: String) =
    lex(input)
        .toPostfixExpressionWhereOperatorsHaveEqualPrecedence()
        .evaluate()

private fun evaluateWherePlusHasGreaterPrecedenceThanMultiply(input: String) =
    lex(input)
        .toPostfixExpressionWherePlusHasGreaterPrecedence()
        .evaluate()

private fun List<Token>.evaluate(): Long {
    val stack: ArrayDeque<Long> = ArrayDeque()
    this.forEach {
        when (it) {
            is IntegerToken -> stack.push(it.value)
            is MultiplyToken -> {
                val rhs = stack.pop()
                val lhs = stack.pop()
                stack.push(lhs * rhs)
            }
            is PlusToken -> {
                val rhs = stack.pop()
                val lhs = stack.pop()
                stack.push(lhs + rhs)
            }
            else -> throw AssertionError(it)
        }
    }

    return stack.pop()
}

fun Sequence<Token>.toPostfixExpressionWhereOperatorsHaveEqualPrecedence(): List<Token> {
    // This is my best attempt at implementing the Shunting yerd algorithm to convert an infix expression to a postfix
    // expression. Assumes all operators have same precedence and are left-associative.
    val stack: ArrayDeque<Token> = ArrayDeque()
    val output: ArrayDeque<Token> = ArrayDeque()

    forEach {
        when (it) {
            is IntegerToken -> output.addLast(it)
            is OperatorToken -> {
                while (stack.isNotEmpty() && stack.peek() is OperatorToken) {
                    output.addLast(stack.pop())
                }
                stack.push(it)
            }
            is OpenBracketToken -> stack.push(it)
            is CloseBracketToken -> {
                while (stack.isNotEmpty() && stack.peek() is OperatorToken) {
                    output.addLast(stack.pop())
                }
                val openBracket = stack.pop()
                if (openBracket !is OpenBracketToken) throw AssertionError(openBracket.toString())
            }
            is SpaceToken -> {}
        }
    }
    while (!stack.isEmpty()) {
        val tok = stack.pop()
        if (tok is OpenBracketToken) throw AssertionError("oh golly")
        output.addLast(tok)
    }

    return output
}

fun Sequence<Token>.toPostfixExpressionWherePlusHasGreaterPrecedence(): List<Token> {
    // This is my best attempt at implementing the Shunting yerd algorithm to convert an infix expression to a postfix
    // expression. Assumes all operators are left-associative.
    val stack: ArrayDeque<Token> = ArrayDeque()
    val output: ArrayDeque<Token> = ArrayDeque()

    forEach {
        when (it) {
            is IntegerToken -> output.addLast(it)
            is OperatorToken -> {
                while (stack.isNotEmpty()
                    && stack.peek() is OperatorToken
                    && (stack.peek() as OperatorToken).hasGreaterOrEqualPrecedenceThan(it)
                ) {
                    output.addLast(stack.pop())
                }
                stack.push(it)
            }
            is OpenBracketToken -> stack.push(it)
            is CloseBracketToken -> {
                while (stack.isNotEmpty() && stack.peek() is OperatorToken) {
                    output.addLast(stack.pop())
                }
                val openBracket = stack.pop()
                if (openBracket !is OpenBracketToken) throw AssertionError(openBracket.toString())
            }
            is SpaceToken -> {}
        }
    }
    while (!stack.isEmpty()) {
        val tok = stack.pop()
        if (tok is OpenBracketToken) throw AssertionError("oh golly")
        output.addLast(tok)
    }

    return output
}


private fun lex(ston: String): Sequence<Token> {
    val tokenSeq = sequence {
        var input: String = ston

        while (input.isNotEmpty()) {
            val c = input.first()
            val token: Token = when (c) {
                in '0'..'9' -> IntegerToken(c.toString().toLong())
                '+' -> PlusToken(c)
                '*' -> MultiplyToken(c)
                '(' -> OpenBracketToken(c)
                ')' -> CloseBracketToken(c)
                ' ' -> SpaceToken(c)
                else -> throw IllegalArgumentException(input)
            }
            yield(token)

            input = input.drop(1)
        }
    }

    return tokenSeq
}

private fun <T> ArrayDeque<T>.push(v: T) = addLast(v)
private fun <T> ArrayDeque<T>.pop() = removeLast()
private fun ArrayDeque<Token>.peek() = last()

sealed interface Token
sealed class OperatorToken : Token {
    abstract fun hasGreaterOrEqualPrecedenceThan(that: OperatorToken): Boolean
}

data class PlusToken(val c: Char) : OperatorToken() {
    override fun hasGreaterOrEqualPrecedenceThan(that: OperatorToken) = true
}

data class MultiplyToken(val c: Char) : OperatorToken() {
    override fun hasGreaterOrEqualPrecedenceThan(that: OperatorToken) = that is MultiplyToken
}

data class IntegerToken(val value: Long) : Token
data class OpenBracketToken(val c: Char) : Token
data class CloseBracketToken(val c: Char) : Token
data class SpaceToken(val c: Char) : Token
