package web.remember.domain.member.application

import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto
import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry

interface MemberService {
    fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto

    fun existsById(id: String): Boolean

    fun findNameAndIndustryById(id: String): ResponseNameAndIndustry
}
