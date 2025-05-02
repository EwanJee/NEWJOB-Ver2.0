package web.remember.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import web.remember.domain.error.CustomException
import web.remember.util.dto.ResponseMemberInfoDto
import web.remember.util.dto.ResponseTokenDto

@Component
class KakaoUtil(
    private val webClientBuilder: WebClient.Builder,
) {
    private val objectMapper = ObjectMapper()

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private var clientId: String = ""

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private var redirectUri: String = ""

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private var clientSecret: String = ""

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
        println("responeTokenDto = $responeTokenDto")
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
        println("memberInfoDto = $memberInfoDto")
        return memberInfoDto
    }
}
