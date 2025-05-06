package web.remember.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import web.remember.domain.error.CustomException
import web.remember.util.dto.KakaoTokenInfo
import web.remember.util.dto.ResponseMemberInfoDto
import web.remember.util.dto.ResponseTokenDto
import java.time.Duration
import java.time.ZonedDateTime

@Component
class KakaoUtil(
    private val webClientBuilder: WebClient.Builder,
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val objectMapper = ObjectMapper()

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private lateinit var clientId: String

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private lateinit var redirectUri: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private lateinit var clientSecret: String

    fun getAccessToken(kakaoId: String): String? {
        val redisKey = "kakao:token:$kakaoId"
        val json = redisTemplate.opsForValue().get(redisKey) ?: return null
        val tokenInfo = objectMapper.readValue(json, KakaoTokenInfo::class.java)

        val now = ZonedDateTime.now()
        val expiresAt = ZonedDateTime.parse(tokenInfo.kakaoTokenExpiresAt)

        return if (now.isBefore(expiresAt)) {
            tokenInfo.kakaoAccessToken
        } else {
            // access token 만료 → refresh token 사용
            val refreshed = refreshToken(tokenInfo.kakaoRefreshToken)
            saveToRedis(kakaoId, refreshed)
            refreshed.kakaoAccessToken
        }
    }

    fun saveToRedis(
        kakaoId: String,
        token: KakaoTokenInfo,
    ) {
        val redisKey = "kakao:token:$kakaoId"
        val ttl = Duration.between(ZonedDateTime.now(), ZonedDateTime.parse(token.kakaoTokenExpiresAt))
        redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(token), ttl)
    }

    fun refreshToken(refreshToken: String): KakaoTokenInfo {
        val webClient: WebClient =
            webClientBuilder
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build()
        val form =
            LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "refresh_token")
                add("client_id", clientId)
                add("refresh_token", refreshToken)
                add("client_secret", clientSecret)
            }

        val response =
            webClient
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(JsonNode::class.java)
                .block()!!

        val accessToken = response["access_token"].asText()
        val expiresIn = response["expires_in"].asLong()
        val newRefreshToken = response["refresh_token"]?.asText() ?: refreshToken
        val expiresAt = ZonedDateTime.now().plusSeconds(expiresIn)

        return KakaoTokenInfo(
            kakaoAccessToken = accessToken,
            kakaoRefreshToken = newRefreshToken,
            kakaoTokenExpiresAt = expiresAt.toString(),
        )
    }

    fun requestToken(accessCode: String): ResponseTokenDto {
        val webClient: WebClient =
            webClientBuilder
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build()
        val formData: MultiValueMap<String, String> =
            LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "authorization_code")
                add("client_id", clientId)
                add("redirect_uri", redirectUri)
                add("code", accessCode)
                add("client_secret", clientSecret)
            }
        println("formData = $formData")
        val response: String =
            webClient
                .post()
                .uri("/oauth/token")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: throw CustomException("토큰을 받아오는 데 실패했습니다.")
        println("response = $response")
        var responeTokenDto: ResponseTokenDto? = null
        try {
            responeTokenDto = objectMapper.readValue(response, ResponseTokenDto::class.java)
        } catch (e: Exception) {
            throw CustomException("토큰을 받아오는 데 실패했습니다.")
        }
        if (responeTokenDto == null) {
            throw CustomException("토큰을 받아오는 데 실패했습니다.")
        }
        return responeTokenDto
    }

    fun requestMemberInfo(accessToken: String): ResponseMemberInfoDto {
        val webClient: WebClient =
            webClientBuilder
                .baseUrl("https://kapi.kakao.com")
                .defaultHeaders {
                    it.acceptCharset = listOf(Charsets.UTF_8)
                    it.contentType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
                }.defaultHeader("Authorization", "Bearer $accessToken")
                .build()
        val response: ResponseEntity<String> =
            webClient
                .get()
                .uri("/v2/user/me")
                .retrieve()
                .toEntity(String::class.java)
                .block() ?: throw CustomException("카카오톡 사용자 정보를 받아오는 데 실패했습니다.")
        var memberInfoDto: ResponseMemberInfoDto? = null
        try {
            memberInfoDto = objectMapper.readValue(response.body, ResponseMemberInfoDto::class.java)
        } catch (e: Exception) {
            throw CustomException("카카오톡 사용자 정보를 받아오는 데 실패했습니다.")
        }
        if (memberInfoDto == null) {
            throw CustomException("카카오톡 사용자 정보를 받아오는 데 실패했습니다.")
        }
        if (!memberInfoDto.isDefaultNickname) {
            memberInfoDto.nickname = "회원"
        }
        return memberInfoDto
    }

    fun logout(accessToken: String) {
        val webClient: WebClient =
            webClientBuilder
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .defaultHeader("Authorization", "Bearer $accessToken")
                .build()
        webClient
            .post()
            .uri("/v1/user/logout")
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw CustomException("카카오톡 로그아웃에 실패했습니다.")
    }
}
