package web.remember.util.dto

data class KakaoTokenInfo(
    val kakaoAccessToken: String,
    val kakaoRefreshToken: String,
    val kakaoTokenExpiresAt: String,
)
