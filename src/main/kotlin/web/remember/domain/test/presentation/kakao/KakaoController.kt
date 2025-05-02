package web.remember.domain.test.presentation.kakao

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import web.remember.domain.member.application.MemberService

@RestController
@RequestMapping("/api/v1/kakao")
class KakaoController(
    private val webClientBuilder: WebClient.Builder,
    private val memberService: MemberService,
) {
    @PostMapping("/sendPdf")
    fun sendPdf(
        @RequestParam("id") memberId: String,
        @RequestBody pdfUrl: String,
    ): ResponseEntity<String> {
        val webClient: WebClient = webClientBuilder.build()
        val accessToken: String = memberService.findKakaoAccessToken(memberId)
        val json =
            """
{
  "object_type": "text",
  "text": "PDF 파일이 생성되었습니다. 아래 링크를 클릭하여 다운로드하세요.",
  "link": {
    "web_url": "$pdfUrl",
    "mobile_web_url": "$pdfUrl"
  }
}
            """.trimIndent()

        val response =
            webClient
                .post()
                .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
                .header("Authorization", "Bearer $accessToken")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("template_object=$json")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

        return ResponseEntity.ok(response ?: "No Response")
    }
}
