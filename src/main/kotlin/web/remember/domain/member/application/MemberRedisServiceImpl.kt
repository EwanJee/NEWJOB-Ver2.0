package web.remember.domain.member.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException

@Service
class MemberRedisServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : MemberRedisService {
    override fun evictToken(kakaoId: String): String {
        val key = "kakao:token:$kakaoId"
        val token = redisTemplate.opsForValue().get(key)
        if (token != null) {
            redisTemplate.delete(key)
        }
        return token ?: throw CustomException("토큰이 존재하지 않습니다.")
    }
}
