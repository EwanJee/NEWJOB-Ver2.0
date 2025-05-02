package web.remember.util.dto

import com.fasterxml.jackson.annotation.JsonProperty

class ResponseMemberInfoDto(
    val id: Long,
    @JsonProperty("connected_at")
    val connectedAt: String,
    @JsonProperty("kakao_account.profile.nickname")
    var nickname: String = "",
    @JsonProperty("kakao_account.email")
    val email: String = "user@kakao.com",
    @JsonProperty("kakao_account.profile.is_default_nickname")
    val isDefaultNickname: Boolean,
)
