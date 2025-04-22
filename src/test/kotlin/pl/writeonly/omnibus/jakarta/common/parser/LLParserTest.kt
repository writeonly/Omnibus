package pl.writeonly.omnibus.jakarta.common.parser

import io.vavr.control.Either
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pl.writeonly.omnibus.jakarta.common.ast.BooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.BooleanOperator
import pl.writeonly.omnibus.jakarta.common.ast.FunctionCall
import pl.writeonly.omnibus.jakarta.common.ast.ListBooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.Literal
import pl.writeonly.omnibus.jakarta.common.ast.RelationalBooleanExpression
import pl.writeonly.omnibus.jakarta.common.ast.RelationalExpression
import pl.writeonly.omnibus.jakarta.common.ast.RelationalOperator

class LLParserTest {

    @Test
    fun `parse simple equality`() {
        val parser = LLParser("(= 1 2)")
        val result = parser.parse()
        val expected = RelationalBooleanExpression(
            RelationalExpression(
                RelationalOperator.EQ,
                Literal(1u),
                Literal(2u)
            )
        )
        assertEquals(Either.right<String, BooleanExpression>(expected), result)
    }

    @Test
    fun `parse nested AND expression`() {
        val parser = LLParser("(& (= 1 2) (= 3 4))")
        val result = parser.parse()
        val expected = ListBooleanExpression(
            BooleanOperator.AND,
            listOf(
                RelationalBooleanExpression(
                    RelationalExpression(
                        RelationalOperator.EQ,
                        Literal(1u),
                        Literal(2u)
                    )
                ),
                RelationalBooleanExpression(
                    RelationalExpression(
                        RelationalOperator.EQ,
                        Literal(3u),
                        Literal(4u)
                    )
                )
            )
        )
        assertEquals(Either.right<String, BooleanExpression>(expected), result)
    }

    @Test
    fun `parse function call in relational`() {
        val parser = LLParser("(> foo 5)")
        val result = parser.parse()
        val expected = RelationalBooleanExpression(
            RelationalExpression(
                RelationalOperator.GT,
                FunctionCall("foo"),
                Literal(5u)
            )
        )
        assertEquals(Either.right<String, BooleanExpression>(expected), result)
    }
}
