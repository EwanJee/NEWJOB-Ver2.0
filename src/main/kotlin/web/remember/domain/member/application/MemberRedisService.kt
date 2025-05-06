package web.remember.domain.member.application

interface MemberRedisService {
    fun evictToken(kakaoId: String): String
}
