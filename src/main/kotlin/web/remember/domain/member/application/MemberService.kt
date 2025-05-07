package web.remember.domain.member.application

import jakarta.servlet.http.HttpServletResponse
import web.remember.domain.member.dto.*
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

    fun findKakaoAccessToken(memberId: String): String

    fun findById(memberId: String): MemberInfoDto
}
