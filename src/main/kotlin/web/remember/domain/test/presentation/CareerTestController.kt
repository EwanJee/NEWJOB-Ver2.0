package web.remember.domain.test.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.application.dto.RequestCreateCareerTestDto
import web.remember.domain.test.dto.RequestScoreUpdateDto

@RestController
@RequestMapping("/api/v1/career-test")
class CareerTestController(
    private val careerTestService: CareerTestService,
    private val memberService: MemberService,
) {
    @PostMapping()
    fun startTest(
        @RequestBody dto: RequestCreateCareerTestDto,
    ): ResponseEntity<String> {
        if (!memberService.existsById(dto.memberId)) {
            throw CustomException("존재하지 않는 회원입니다")
        }
        val testId = careerTestService.startTest(dto.questionMap, dto.memberId)
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
