package pl.writeonly.omnibus.apigateway.handler
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
class GlobalErrorHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {

    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable
    ): Mono<Void> {

        val response = exchange.response
        response.statusCode = HttpStatus.BAD_REQUEST

        val error = ErrorResponse(
            code = "ERROR",
            message = ex.message ?: "Unknown error"
        )

        val bytes = objectMapper.writeValueAsBytes(error)

        val buffer = response.bufferFactory().wrap(bytes)

        response.headers.contentType = MediaType.APPLICATION_JSON

        return response.writeWith(Mono.just(buffer))
    }
}
