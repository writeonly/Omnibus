package pl.writeonly.omnibus.jakarta.common.parser

import pl.writeonly.omnibus.jakarta.common.ast.BooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.BooleanOperator
import pl.writeonly.omnibus.jakarta.common.ast.FunctionCall
import pl.writeonly.omnibus.jakarta.common.ast.ListBooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.Literal
import pl.writeonly.omnibus.jakarta.common.ast.RelationalBooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.RelationalExpression
import pl.writeonly.omnibus.jakarta.common.ast.RelationalOperator
import pl.writeonly.omnibus.jakarta.common.ast.Value

class LLParser(input: String) {

    private var index = 0
    private val tokens = tokenize(input)

    fun parse(): BooleanExpression {
        return parseBooleanExpression()
    }

    private fun parseBooleanExpression(): BooleanExpression {
        val token = peek()

        return when (token) {
            "&" -> {
                consume() // consume AND
                val left = parseBooleanExpression()
                val right = parseBooleanExpression()
                ListBooleanExpression(BooleanOperator.AND, listOf(left, right))
            }
            "|" -> {
                consume() // consume OR
                val left = parseBooleanExpression()
                val right = parseBooleanExpression()
                ListBooleanExpression(BooleanOperator.OR, listOf(left, right))
            }
            else -> {
                val relational = parseRelationalExpression()
                RelationalBooleanExpression(relational)
            }
        }
    }

    private fun parseRelationalExpression(): RelationalExpression {
        val operator = parseRelationalOperator()
        val left = parseValue()
        val right = parseValue()
        return RelationalExpression(operator, left, right)
    }

    private fun parseRelationalOperator(): RelationalOperator {
        return when (val token = consume()) {
            "=" -> RelationalOperator.EQ
            "<>" -> RelationalOperator.NE
            "<" -> RelationalOperator.LT
            ">" -> RelationalOperator.GT
            "<=" -> RelationalOperator.LE
            ">=" -> RelationalOperator.GE
            else -> throw IllegalArgumentException("Unexpected relational operator: $token")
        }
    }

    private fun parseValue(): Value {
        val token = peek() ?: throw IllegalArgumentException("Unexpected end of input")

        return when {
            token.matches(Regex("\\d+")) -> {
                Literal(consume().toUInt())
            }
            token.matches(Regex("[a-zA-Z_]\\w*")) -> {
                FunctionCall(consume())
            }
            else -> throw IllegalArgumentException("Unexpected value: $token")
        }
    }

    private fun tokenize(input: String): List<String> {
        val regex = """\(|\)|\b(&|\|==|<>|<|>|<=|>=)\b|\d+|[a-zA-Z_]\w*""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }

    private fun consume(): String {
        val token = peek()
        if (token != null) {
            index++
        }
        return token ?: throw IllegalArgumentException("Unexpected end of input")
    }

    private fun peek(): String? {
        return if (index < tokens.size) tokens[index] else null
    }
}
