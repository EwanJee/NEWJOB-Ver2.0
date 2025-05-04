package web.remember.domain.test.presentation.kakao

import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.util.JwtUtil

@RestController
@RequestMapping("/api/v1/kakao")
class KakaoController(
    private val webClientBuilder: WebClient.Builder,
    private val memberService: MemberService,
    private val jwtUtil: JwtUtil,
) {
    @Suppress("ktlint:standard:max-line-length")
    @PostMapping("/sendPdf")
    fun sendPdf(
        @CookieValue("jwt") jwt: String,
        @RequestBody pdfUrl: String,
    ): ResponseEntity<String> {
        if (!pdfUrl.startsWith("https")) {
            throw CustomException("PDF URL이 유효하지 않습니다.")
        }

        // 메서드 두번 호출 방지
        val claims = jwtUtil.parseClaims(jwt)
        val memberId: String =
            claims["kakaoId"]?.toString()
                ?: throw CustomException("세션 정보가 없습니다. 로그인 후, 다시 실행해주세요")
        val webClient: WebClient = webClientBuilder.build()
        val accessToken: String = memberService.findKakaoAccessToken(memberId)
        val imageUrl = "https://picsum.photos/640/640"
        val templateObject =
            """
            {
              "object_type": "feed",
              "content": {
                "title": "PDF 파일 안내",
                "description": "검사 결과가 PDF로 생성되었습니다.",
                "image_url": "$imageUrl",
                "link": {
                  "web_url": "https://remembercareer.com/career/result?pdfUrl=$pdfUrl",
                  "mobile_web_url": "https://remembercareer.com/career/result?pdfUrl=$pdfUrl"
                }
              }
            }
            """.trimIndent()

        val multiValueMap = LinkedMultiValueMap<String, String>()
        multiValueMap.add("template_object", templateObject)

        val response =
            webClient
                .post()
                .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
                .header("Authorization", "Bearer $accessToken")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(BodyInserters.fromFormData(multiValueMap))
                .retrieve()
                .onStatus({ it.isError }) {
                    it.bodyToMono(String::class.java).map { errorBody ->
                        throw Exception("Error: $errorBody")
                    }
                }.bodyToMono(String::class.java)
                .block()
        return ResponseEntity.ok(response ?: "No Response")
    }
}
