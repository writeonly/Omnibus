package pl.writeonly.omnibus.workflow.api

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.converter.HttpMessageNotReadableException
import java.time.Instant

@RestControllerAdvice
class ApiErrorHandler {
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            ConstraintViolationException::class,
            MethodArgumentNotValidException::class,
            HttpMessageNotReadableException::class,
        ],
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(exception: Exception): Map<String, Any?> =
        mapOf(
            "timestamp" to Instant.now().toString(),
            "message" to exception.message,
        )

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    fun handleIllegalState(exception: IllegalStateException): Map<String, Any?> =
        mapOf(
            "timestamp" to Instant.now().toString(),
            "message" to exception.message,
        )
}
