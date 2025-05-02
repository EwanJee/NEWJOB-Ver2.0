package web.remember.domain.member.application

import jakarta.servlet.http.HttpServletResponse
import web.remember.domain.member.dto.RequestAuthLoginDto
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto
import web.remember.domain.member.dto.ResponseKakaoMemberDto
import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry

interface MemberService {
    fun create(member: RequestAuthLoginDto): ResponseCreateMemberDto

    fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto

    fun existsById(id: String): Boolean

    fun findNameAndIndustryById(id: String): ResponseNameAndIndustry

    fun existsByPhoneNumberAndName(
        phoneNumber: String,
        name: String,
    ): Boolean

    fun oAuthLogin(
        code: String,
        response: HttpServletResponse,
    ): ResponseKakaoMemberDto
}
