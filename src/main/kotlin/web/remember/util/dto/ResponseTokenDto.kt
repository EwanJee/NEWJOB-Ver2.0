package web.remember.util.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseTokenDto(
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("id_token")
    val idToken: String = "",
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Long,
    @JsonProperty("scope")
    val scope: String = "",
)
