package web.remember.domain.question.presentation

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.error.CustomException
import web.remember.domain.question.application.QuestionService
import web.remember.domain.question.application.career.CareerService
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/questions")
class QuestionController(
    private val questionService: QuestionService,
    private val careerService: CareerService,
    private val jwtUtil: JwtUtil,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<Long> {
        val questionId = questionService.create(file)
        return ResponseEntity.ok(questionId)
    }

    @PostMapping("/career")
    fun makeCareerQuestion(
        @CookieValue("jwt") jwt: String?,
    ): ResponseEntity<Map<String, Map<String, String>>> {
        if (jwt == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val claims = jwtUtil.parseClaims(jwt).toMutableMap()
        val memberId = claims["memberId"] as String? ?: throw CustomException("로그인 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        val question = careerService.makeQuestion()
        return ResponseEntity.ok(question)
    }
}
