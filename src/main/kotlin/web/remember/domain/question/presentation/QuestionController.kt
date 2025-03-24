package web.remember.domain.question.presentation

import jakarta.servlet.http.HttpSession
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.error.CustomException
import web.remember.domain.question.application.QuestionService
import web.remember.domain.question.application.career.CareerService

@RestController
@RequestMapping("/api/v1/questions")
class QuestionController(
    private val questionService: QuestionService,
    private val careerService: CareerService,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<Long> {
        val questionId = questionService.create(file)
        return ResponseEntity.ok(questionId)
    }

    @PostMapping("/career")
    fun makeCareerQuestion(httpSession: HttpSession): ResponseEntity<Map<String, Map<String, String>>> {
        if (httpSession.getAttribute("memberId") == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        val question = careerService.makeQuestion()
        return ResponseEntity.ok(question)
    }
}
