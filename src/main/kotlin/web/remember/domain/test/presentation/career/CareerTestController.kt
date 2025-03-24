package web.remember.domain.test.presentation.career

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.test.application.career.CareerTestService
import web.remember.domain.test.presentation.career.dto.RequestCreateCareerTestDto
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
        @SessionAttribute("memberId", required = false) memberId: String?,
        httpSession: HttpSession,
    ): ResponseEntity<String> {
        if (memberId == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        if (!memberService.existsById(memberId)) {
            throw CustomException("존재하지 않는 회원입니다")
        }
        val testId = careerTestService.startTest(dto.questionMap, memberId)
        httpSession.setAttribute("testId", testId)
        return ResponseEntity.ok(testId)
    }

    @PutMapping("/score")
    fun updateScore(
        @RequestBody @Valid dto: RequestScoreUpdateDto,
        @SessionAttribute("memberId", required = false) memberId: String?,
        @SessionAttribute("testId", required = false) testId: String?,
    ) {
        if (memberId == null || testId == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        dto.update(memberId, testId)
        careerTestService.updateScore(dto)
    }

    @PutMapping("/age")
    fun updateCareerAge(
        @RequestBody @Valid dto: RequestUpdateCareerAge,
        @SessionAttribute("memberId", required = false) memberId: String?,
        @SessionAttribute("testId", required = false) testId: String?,
    ) {
        if (memberId == null || testId == null) {
            throw CustomException("로그인이 필요합니다.")
        }
        careerTestService.updateCareerAge(testId, memberId, dto.age)
    }

    @PutMapping("/finish")
    fun finishTest(
        @SessionAttribute("memberId") memberId: String,
        @SessionAttribute("testId") testId: String,
    ) {
        careerTestService.finishTest(memberId, testId)
    }
}
