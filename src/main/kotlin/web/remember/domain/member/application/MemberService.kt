package web.remember.domain.member.application

import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto

interface MemberService {
    fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto

    fun existsById(id: String): Boolean
}
