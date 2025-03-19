package web.remember.domain.question.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.question.application.QuestionService
import web.remember.domain.question.application.career.CareerService

@RestController
@RequestMapping("/api/v1/questions")
class QuestionController(
    private val questionService: QuestionService,
    private val careerService: CareerService,
) {
    @PostMapping()
    fun create(file: MultipartFile): ResponseEntity<Long> {
        val questionId = questionService.create(file)
        return ResponseEntity.ok(questionId)
    }

    @PostMapping("/career")
    fun makeCareerQuestion(): ResponseEntity<Map<String, List<String>>> {
        val question = careerService.makeQuestion()
        return ResponseEntity.ok(question)
    }
}
