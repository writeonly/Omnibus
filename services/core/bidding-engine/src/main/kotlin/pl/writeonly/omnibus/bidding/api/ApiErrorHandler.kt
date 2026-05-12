package pl.writeonly.omnibus.bidding.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class ApiErrorHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(exception: IllegalArgumentException): ResponseEntity<Map<String, Any?>> =
        build(HttpStatus.BAD_REQUEST, exception.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(@Suppress("UNUSED_PARAMETER") exception: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> =
        build(HttpStatus.BAD_REQUEST, "Request validation failed")

    private fun build(status: HttpStatus, message: String?): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.status(status).body(
            mapOf(
                "timestamp" to OffsetDateTime.now().toString(),
                "status" to status.value(),
                "error" to status.reasonPhrase,
                "message" to message,
            ),
        )
}
