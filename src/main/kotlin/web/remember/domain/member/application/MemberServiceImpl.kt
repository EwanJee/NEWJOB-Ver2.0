package web.remember.domain.member.application

import jakarta.servlet.http.HttpServletResponse
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import web.remember.domain.error.CustomException
import web.remember.domain.member.dto.*
import web.remember.domain.member.entity.Member
import web.remember.domain.member.repository.MemberRepository
import web.remember.domain.member.repository.jdsl.dto.ResponseNameAndIndustry
import web.remember.util.JwtUtil
import web.remember.util.KakaoUtil
import web.remember.util.dto.ResponseMemberInfoDto
import web.remember.util.dto.ResponseTokenDto

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val kakaoUtil: KakaoUtil,
    private val jwtUtil: JwtUtil,
    private val encoder: PasswordEncoder,
    private val redisTemplate: RedisTemplate<String, String>,
) : MemberService {
    @Transactional
    override fun create(member: RequestAuthLoginDto): ResponseCreateMemberDto {
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
        memberEntity.emailL = member.email
        memberRepository.save(memberEntity)
        return ResponseCreateMemberDto(
            id = memberEntity.id,
            name = member.name,
            phoneNumber = member.phoneNumber,
            industry = member.industry,
        )
    }

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

    override fun oAuthLogin(
        code: String,
        response: HttpServletResponse,
    ): ResponseKakaoMemberDto {
        val token: ResponseTokenDto = kakaoUtil.requestToken(code)
        val memberInfo: ResponseMemberInfoDto = kakaoUtil.requestMemberInfo(token.accessToken)
        val kakaoId = memberInfo.id
        val memberEntity: Member = memberRepository.findByKakaoId(kakaoId) ?: create(memberInfo)
        val jwtToken: String = jwtUtil.createAccessToken(memberEntity.id, "ROLE_USER")
        response.setHeader("Authorization", jwtToken)
        val dto = ResponseKakaoMemberDto.of(memberEntity)
        dto.token = "Bearer <JWT_TOKEN>"
        return dto
    }

    override fun findKakaoAccessToken(memberId: String): String {
        val redisKey = "kakao:token:$memberId"
        val value = redisTemplate.opsForValue().get(redisKey) ?: throw CustomException("만료 시간이 지났습니다. 다시 로그인 해주세요.")
        val kakaoAccessToken =
            value.substringAfter("\"kakaoAccessToken\": \"").substringBefore("\"")
        return kakaoAccessToken
    }

    override fun findById(memberId: String): MemberInfoDto {
        val member: Member =
            memberRepository.findById(memberId).orElseThrow {
                CustomException("해당 멤버가 존재하지 않습니다.")
            }
        val dto: MemberInfoDto =
            MemberInfoDto(
                name = member.name,
                phoneNumber = member.phoneNumber,
                email = member.emailL,
            )
        return dto
    }

    private fun create(memberInfo: ResponseMemberInfoDto): Member {
        val member: Member =
            Member(
                name = memberInfo.nickname,
                phoneNumber = "",
                industry = "",
            )
        member.emailL = memberInfo.email
        member.kakaoId = memberInfo.id
        return memberRepository.save(member)
    }
}
