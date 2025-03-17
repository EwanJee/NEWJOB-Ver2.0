package web.remember.domain.member.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import web.remember.domain.member.application.MemberService
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto

@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping()
    fun create(requestDto: RequestCreateMemberDto): ResponseCreateMemberDto = memberService.create(requestDto)
}
