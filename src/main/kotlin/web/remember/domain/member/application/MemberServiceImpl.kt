package web.remember.domain.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.member.dto.RequestCreateMemberDto
import web.remember.domain.member.dto.ResponseCreateMemberDto
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
) : MemberService {
    @Transactional
    override fun create(member: RequestCreateMemberDto): ResponseCreateMemberDto {
        val isExist = memberRepository.existsByPhoneNumber(member.phoneNumber)
        if (isExist) {
            throw CustomException("이미 존재하는 회원입니다")
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
}
