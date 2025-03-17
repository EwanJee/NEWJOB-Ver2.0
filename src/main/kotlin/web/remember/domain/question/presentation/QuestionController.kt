package web.remember.domain.question.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import web.remember.domain.question.application.QuestionService

@RestController
@RequestMapping("/api/questions")
class QuestionController(
    val questionService: QuestionService,
) {
    @PostMapping()
    fun create(file: MultipartFile): ResponseEntity<Long> {
        val questionId = questionService.create(file)
        return ResponseEntity.ok(questionId)
    }
}
