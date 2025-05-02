@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.member.presentation

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import web.remember.domain.member.application.MemberService
import web.remember.domain.member.dto.RequestAuthLoginDto
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto

@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {
    val logger: Logger = org.slf4j.LoggerFactory.getLogger(MemberController::class.java)

    @PostMapping()
    fun approach(
        @RequestBody @Valid requestDto: RequestCreateMemberDto,
        httpSession: HttpSession,
    ): ResponseEntity<ResponseCreateMemberDto> {
        val responseDto = memberService.create(requestDto)
        httpSession.setAttribute("memberId", responseDto.id)
        return ResponseEntity.ok(responseDto)
    }

    @PostMapping("/login")
    fun authLogin(
        @RequestBody dto: RequestAuthLoginDto,
        httpSession: HttpSession,
    ): ResponseEntity<ResponseCreateMemberDto> {
        val responseDto = memberService.create(dto)
        logger.info("Response DTO: $responseDto") // DTO의 내용을 로그로 출력
        httpSession.setAttribute("memberId", responseDto.id)
        return ResponseEntity.ok(responseDto)
    }
}
