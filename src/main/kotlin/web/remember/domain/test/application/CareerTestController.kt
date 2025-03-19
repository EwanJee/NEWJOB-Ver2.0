package web.remember.domain.test.application

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import web.remember.domain.test.dto.RequestScoreUpdateDto
import web.remember.domain.test.presentation.career.CareerTestService

@RestController
@RequestMapping("/api/v1/career-test")
class CareerTestController(
    private val careerTestService: CareerTestService,
) {
    @PostMapping()
    fun startTest(
        questionMap: Map<String, List<String>>,
        memberId: String,
    ): ResponseEntity<String> {
        val testId = careerTestService.startTest(questionMap, memberId)
        return ResponseEntity.ok(testId)
    }

    @PutMapping("/score")
    fun updateScore(dto: RequestScoreUpdateDto) {
        careerTestService.updateScore(dto)
    }

    @PutMapping("/age")
    fun updateCareerAge(
        testId: String,
        memberId: String,
        age: String,
    ) {
        careerTestService.updateCareerAge(testId, memberId, age)
    }

    @PutMapping("/finish")
    fun finishTest(
        memberId: String,
        testId: String,
    ) {
        careerTestService.finishTest(memberId, testId)
    }
}
