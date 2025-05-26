package web.remember.domain.test.stream

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import web.remember.domain.error.CustomException
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/stream")
class TestStreamController(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper,
    private val testStreamService: TestStreamService,
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(TestStreamController::class.java)

    @GetMapping("/pending-test")
    fun getPendingTest(): String = "Pending Test"

    @GetMapping("/{testId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamCareerPdfStatus(
        @PathVariable testId: Long,
        @CookieValue("jwt") jwt: String,
    ): ResponseEntity<SseEmitter> {
        val claims = jwtUtil.parseClaims(jwt)
        val memberId =
            claims["memberId"] as String?
                ?: throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        val pdfId = "CareerTest_$memberId:$testId"
        val emitter = SseEmitter(60_000L)
        val context = SecurityContextHolder.getContext()
        testStreamService.emit(
            emitter,
            pdfId,
            context,
        )
        return ResponseEntity.ok().body(emitter)
    }
}
