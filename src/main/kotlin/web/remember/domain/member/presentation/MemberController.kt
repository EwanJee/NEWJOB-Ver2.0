package web.remember.domain.member.presentation

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import web.remember.domain.member.application.MemberService
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.RequestLoginDto
import web.remember.domain.member.dto.ResponseCreateMemberDto

@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping()
    fun approach(
        @RequestBody @Valid requestDto: RequestCreateMemberDto,
        httpSession: HttpSession,
    ): ResponseEntity<ResponseCreateMemberDto> {
        val responseDto = memberService.create(requestDto)
        httpSession.setAttribute("memberId", responseDto.id)
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping()
    fun login(
        @RequestBody @Valid dto: RequestLoginDto,
    ): ResponseEntity<Boolean> = ResponseEntity.ok(memberService.existsByPhoneNumberAndName(dto.phoneNumber, dto.name))
}
