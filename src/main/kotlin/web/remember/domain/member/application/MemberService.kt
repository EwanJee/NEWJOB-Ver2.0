package web.remember.domain.member.application

import org.springframework.stereotype.Service
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto

@Service
interface MemberService {
    fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto
}
