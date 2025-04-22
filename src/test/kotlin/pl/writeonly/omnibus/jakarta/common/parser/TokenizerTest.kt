package pl.writeonly.omnibus.jakarta.common.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenizerTest {

    private fun tokenize(input: String): List<String> {
        val parser = LLParser("")
        val method = parser.javaClass.getDeclaredMethod("tokenize", String::class.java)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(parser, input) as List<String>
    }

    @Test
    fun `should tokenize simple relational expression`() {
        val input = "= 3 2"
        val expected = listOf("=", "3", "2")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should tokenize logical AND expression`() {
        val input = "& = 1 1 = 2 2"
        val expected = listOf("&", "=", "1", "1", "=", "2", "2")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should tokenize with parentheses`() {
        val input = "(| (< 1 2) (> 3 4))"
        val expected = listOf("(", "|", "(", "<", "1", "2", ")", "(", ">", "3", "4", ")", ")")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should tokenize function calls`() {
        val input = "= f g"
        val expected = listOf("=", "f", "g")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should handle all relational operators`() {
        val input = "= <> < > <= >="
        val expected = listOf("=", "<>", "<", ">", "<=", ">=")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should ignore extra whitespace`() {
        val input = "  (   =  42   f   )  "
        val expected = listOf("(", "=", "42", "f", ")")
        assertEquals(expected, tokenize(input))
    }

    @Test
    fun `should return empty list on empty input`() {
        val input = ""
        val expected = emptyList<String>()
        assertEquals(expected, tokenize(input))
    }
}
