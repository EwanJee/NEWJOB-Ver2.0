package web.remember.domain.test.presentation.career

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.presentation.career.dto.RequestCreateCareerTestDto
import web.remember.domain.test.presentation.career.dto.RequestFinishTest
import web.remember.domain.test.presentation.career.dto.RequestScoreUpdateDto
import web.remember.domain.test.presentation.career.dto.RequestUpdateCareerAge

@RestController
@RequestMapping("/api/v1/career-test")
class CareerTestController(
    private val careerTestService: CareerTestService,
    private val memberService: MemberService,
) {
    @PostMapping()
    fun startTest(
        @RequestBody @Valid dto: RequestCreateCareerTestDto,
    ): ResponseEntity<String> {
        if (!memberService.existsById(dto.memberId)) {
            throw CustomException("존재하지 않는 회원입니다")
        }
        val testId = careerTestService.startTest(dto.questionMap, dto.memberId)
        return ResponseEntity.ok(testId)
    }

    @PutMapping("/score")
    fun updateScore(
        @RequestBody @Valid dto: RequestScoreUpdateDto,
    ) {
        careerTestService.updateScore(dto)
    }

    @PutMapping("/age")
    fun updateCareerAge(
        @RequestBody @Valid dto: RequestUpdateCareerAge,
    ) {
        careerTestService.updateCareerAge(dto.testId, dto.memberId, dto.age)
    }

    @PutMapping("/finish")
    fun finishTest(
        @RequestBody @Valid dto: RequestFinishTest,
    ) {
        careerTestService.finishTest(dto.memberId, dto.testId)
    }
}
