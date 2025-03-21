package web.remember.domain.error.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import web.remember.domain.error.CustomException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleCustomException(ex: CustomException): ResponseEntity<Map<String, String>> {
        val response =
            mapOf(
                "error" to ex.message,
                "status" to HttpStatus.BAD_REQUEST.toString(),
                "timestamp" to LocalDateTime.now().toString(),
            )
        return ResponseEntity.badRequest().body(response)
    }
}
