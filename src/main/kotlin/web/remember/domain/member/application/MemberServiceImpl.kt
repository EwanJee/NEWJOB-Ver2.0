package web.remember.domain.member.application

import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository

class MemberServiceImpl(
    private val memberRepository: MemberRepository,
) : MemberService {
    override fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto {
        val memberEntity = memberRepository.save(Member(name = member.name, phoneNumber = member.phoneNumber))
        return ResponseCreateMemberDto(
            id = memberEntity.id,
            name = memberEntity.name,
            phoneNumber = memberEntity.phoneNumber,
        )
    }
}
