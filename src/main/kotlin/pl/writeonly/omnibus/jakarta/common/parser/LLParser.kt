package pl.writeonly.omnibus.jakarta.common.parser

import io.vavr.control.Either
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

    fun parse(): Either<String, BooleanExpression> =
        parseBooleanExpression()

    private fun parseBooleanExpression(): Either<String, BooleanExpression> =
        peek().flatMap { token ->
            when (token) {
                "&" -> parseBooleanExpression(BooleanOperator.AND)
                "|" -> parseBooleanExpression(BooleanOperator.OR)
                else -> parseRelationalExpression().map(::RelationalBooleanExpression)
            }
        }

    private fun parseBooleanExpression(operator: BooleanOperator) = consume()
        .flatMap { parseBooleanExpression() }
        .flatMap { left ->
            parseBooleanExpression().map { right ->
                ListBooleanExpression(
                    operator,
                    listOf(left, right)
                )
            }
        }

    private fun parseRelationalExpression(): Either<String, RelationalExpression> =
        parseRelationalOperator()
            .flatMap { op ->
                parseValue()
                    .flatMap { left ->
                        parseValue().map { right -> RelationalExpression(op, left, right) }
                    }
            }

    private fun parseRelationalOperator(): Either<String, RelationalOperator> =
        consume().flatMap { token ->
            when (token) {
                "=" -> Either.right(RelationalOperator.EQ)
                "<>" -> Either.right(RelationalOperator.NE)
                "<" -> Either.right(RelationalOperator.LT)
                ">" -> Either.right(RelationalOperator.GT)
                "<=" -> Either.right(RelationalOperator.LE)
                ">=" -> Either.right(RelationalOperator.GE)
                else -> Either.left("Unexpected relational operator: $token")
            }
        }

    private fun parseValue(): Either<String, Value> =
        peek().flatMap { token ->
            when {
                token.matches(Regex("\\d+")) -> consume().map { Literal(it.toUInt()) }
                token.matches(Regex("[a-zA-Z_]\\w*")) -> consume().map(::FunctionCall)
                else -> Either.left("Unexpected value: $token")
            }
        }

    private fun tokenize(input: String): List<String> =
        """\(|\)|<=|>=|<>|=|<|>|\d+|[a-zA-Z_]\w*|&|\|"""
            .toRegex()
            .findAll(input)
            .map { it.value }
            .toList()

    private fun consume(): Either<String, String> =
        if (index < tokens.size) {
            Either.right(tokens[index++])
        } else {
            Either.left("Unexpected end of input")
        }

    private fun peek(): Either<String, String> =
        if (index < tokens.size) {
            Either.right(tokens[index])
        } else {
            Either.left("Unexpected end of input")
        }
}
