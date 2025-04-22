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

    private val initialState = ParseState(tokenize(input), 0)

    fun parse(): Error<BooleanExpression> =
        parseBooleanExpression(initialState).map { it.second }

    private fun parseBooleanExpression(state: ParseState): Parser<BooleanExpression> =
        state.peek().flatMap { token ->
            when (token) {
                "&" -> parseBooleanListExpression(state, BooleanOperator.AND)
                "|" -> parseBooleanListExpression(state, BooleanOperator.OR)
                else -> parseRelationalExpression(state).map { (s, rel) -> s to RelationalBooleanExpression(rel) }
            }
        }

    private fun parseBooleanListExpression(state: ParseState, op: BooleanOperator): Parser<ListBooleanExpression> =
        state.consume().flatMap { (s1, _) ->
            parseBooleanExpression(s1).flatMap { (s2, left) ->
                parseBooleanExpression(s2).map { (s3, right) ->
                    s3 to ListBooleanExpression(op, listOf(left, right))
                }
            }
        }

    private fun parseRelationalExpression(state: ParseState): Parser<RelationalExpression> =
        parseRelationalOperator(state).flatMap { (s1, op) ->
            parseValue(s1).flatMap { (s2, left) ->
                parseValue(s2).map { (s3, right) ->
                    s3 to RelationalExpression(op, left, right)
                }
            }
        }

    private fun parseRelationalOperator(state: ParseState): Parser<RelationalOperator> =
        state.consume().flatMap { (s, token) ->
            when (token) {
                "=" -> Either.right(s to RelationalOperator.EQ)
                "<>" -> Either.right(s to RelationalOperator.NE)
                "<" -> Either.right(s to RelationalOperator.LT)
                ">" -> Either.right(s to RelationalOperator.GT)
                "<=" -> Either.right(s to RelationalOperator.LE)
                ">=" -> Either.right(s to RelationalOperator.GE)
                else -> Either.left("Unexpected relational op: $token")
            }
        }

    private fun parseValue(state: ParseState): Parser<Value> =
        state.peek().flatMap { token ->
            when {
                token.matches(Regex("\\d+")) ->
                    state.consume().map { (s, t) -> s to Literal(t.toUInt()) }
                token.matches(Regex("[a-zA-Z_]\\w*")) ->
                    state.consume().map { (s, t) -> s to FunctionCall(t) }
                else -> Either.left("Unexpected value: $token")
            }
        }

    private fun tokenize(input: String): List<String> =
        """\\(|\\)|<=|>=|<>|=|<|>|\\d+|[a-zA-Z_]\\w*|&|\\|""".toRegex()
            .findAll(input)
            .map { it.value }
            .toList()
}
