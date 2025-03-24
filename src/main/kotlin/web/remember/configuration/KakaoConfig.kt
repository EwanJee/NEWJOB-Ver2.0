package web.remember.configuration

import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KakaoConfig {
//    @Value("\${kakao.api.key}")
    private var kakaoAPiKEy: String? = null

//    @Value("\${kakao.api.secret}")
    private var kakaoApiSecret: String? = null

    //    fun defaultMessageService(): DefaultMessageService = NurigoApp.initialize(kakaoAPiKEy, kakaoApiSecret, "https://api.coolsms.co.kr")
    @Bean
    fun defaultMessageService(): DefaultMessageService = NurigoApp.initialize("kakaoAPiKEy", "", "https://api.coolsms.co.kr")
}
