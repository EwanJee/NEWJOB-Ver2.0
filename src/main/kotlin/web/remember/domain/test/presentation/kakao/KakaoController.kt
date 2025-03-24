package web.remember.domain.test.presentation.kakao

import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/kakao")
class KakaoController(
    private final val kakaoService: DefaultMessageService,
) {
    @PostMapping("/send")
    fun sendKakaoMessage(): SingleMessageSentResponse? {
        // 등록하신 카카오 비즈니스 채널의 pfId를 입력해주세요.
        // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
        val kakaoOption =
            KakaoOption(
                pfId = "pfId 입력",
                templateId = "templateId 입력",
                // disableSms를 true로 설정하실 경우 문자로 대체발송 되지 않습니다.
                disableSms = true,
                // 알림톡 템플릿 내에 #{변수} 형태가 존재할 경우 variables를 설정해주세요.
                // variables = variables,
            )

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        val message =
            Message(
                from = "01094118219",
                to = "수신번호 입력",
                kakaoOptions = kakaoOption,
            )
        val response = kakaoService.sendOne(SingleMessageSendingRequest(message))
        return response
    }
}
