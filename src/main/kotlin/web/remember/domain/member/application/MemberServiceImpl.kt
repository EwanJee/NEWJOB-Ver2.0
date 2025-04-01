package web.remember.domain.member.application

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
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

        evictAllMemberCaches()

        val memberEntity =
            Member(
                name = member.name,
                phoneNumber = member.phoneNumber,
                industry = member.industry,
            )
        memberRepository.save(memberEntity)
        return ResponseCreateMemberDto(
            id = memberEntity.id,
            name = member.name,
            phoneNumber = member.phoneNumber,
            industry = member.industry,
        )
    }

    @Caching(
        evict = [
            CacheEvict(value = ["member-details"], allEntries = true),
            CacheEvict(value = ["member-exists"], allEntries = true),
        ],
    )
    fun evictAllMemberCaches() {
        // 캐시 무효화만을 위한 메서드이므로 내용은 비어있음
    }

    @Cacheable(value = ["member-exists"], key = "#id")
    override fun existsById(id: String): Boolean = memberRepository.existsById(id)

    @Cacheable(value = ["member-details"], key = "#id")
    override fun findNameAndIndustryById(id: String): ResponseNameAndIndustry = memberRepository.findNameAndIndustryById(id)

    override fun existsByPhoneNumberAndName(
        phoneNumber: String,
        name: String,
    ): Boolean = memberRepository.existsByPhoneNumberAndName(phoneNumber, name)
}
