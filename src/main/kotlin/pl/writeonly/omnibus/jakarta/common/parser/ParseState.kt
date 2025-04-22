package pl.writeonly.omnibus.jakarta.common.parser

import io.vavr.control.Either

typealias Error<A> = Either<String, A>

typealias Wrapper<A> = Pair<ParseState, A>

typealias Parser<A> = Error<Wrapper<A>>

data class ParseState(val tokens: List<String>, val index: Int) {
    fun peek(): Error<String> =
        if (index < tokens.size) {
            Either.right(tokens[index])
        } else {
            Either.left("Unexpected end of input")
        }

    fun consume(): Parser<String> =
        if (index < tokens.size) {
            Either.right(copy(index = index + 1) to tokens[index])
        } else {
            Either.left("Unexpected end of input")
        }

    fun consumeExpected(expected: String): Parser<String> =
        consume().flatMap { (nextState, token) ->
            if (token == expected) Either.right(nextState to token)
            else Either.left("Expected '$expected' but got '$token'")
        }
}
