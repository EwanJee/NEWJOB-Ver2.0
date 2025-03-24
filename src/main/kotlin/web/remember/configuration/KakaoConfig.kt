package web.remember.configuration

// @Configuration
class KakaoConfig {
    //    @Value("\${kakao.api.key}")
    private var kakaoAPiKEy: String? = null

    //    @Value("\${kakao.api.secret}")
    private var kakaoApiSecret: String? = null

    //    fun defaultMessageService(): DefaultMessageService = NurigoApp.initialize(kakaoAPiKEy, kakaoApiSecret, "https://api.coolsms.co.kr")
//    @Bean
//    fun defaultMessageService(): DefaultMessageService = NurigoApp.initialize("kakaoAPiKEy", "", "https://api.coolsms.co.kr")
}
