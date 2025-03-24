package web.remember.domain.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository
import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
) : MemberService {
    @Transactional
    override fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto {
        val memberId = memberRepository.findIdByPhoneNumber(member.phoneNumber)
        if (memberId != null) {
            return ResponseCreateMemberDto(
                id = memberId,
                name = member.name,
                phoneNumber = member.phoneNumber,
                industry = member.industry,
            )
        }
        val memberEntity =
            Member(
                name = member.name,
                phoneNumber = member.phoneNumber,
                industry = member.industry,
            )
        memberRepository.save(memberEntity) // 반환값 사용 X
        return ResponseCreateMemberDto(
            id = memberEntity.id,
            name = member.name,
            phoneNumber = member.phoneNumber,
            industry = member.industry,
        )
    }

    override fun existsById(id: String): Boolean = memberRepository.existsById(id)

    override fun findNameAndIndustryById(id: String): ResponseNameAndIndustry = memberRepository.findNameAndIndustryById(id)

    override fun existsByPhoneNumberAndName(
        phoneNumber: String,
        name: String,
    ): Boolean = memberRepository.existsByPhoneNumberAndName(phoneNumber, name)
}
