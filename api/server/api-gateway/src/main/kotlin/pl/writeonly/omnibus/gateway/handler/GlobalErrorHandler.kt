package pl.writeonly.omnibus.gateway.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalErrorHandler(
    private val objectMapper: ObjectMapper
) {

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<ErrorResponse> {

        val error = ErrorResponse(
            code = "ERROR",
            message = ex.message ?: "Unknown error"
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }
}
