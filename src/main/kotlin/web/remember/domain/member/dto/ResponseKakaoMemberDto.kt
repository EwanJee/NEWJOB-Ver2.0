package web.remember.domain.member.dto

import web.remember.domain.member.entity.Member

class ResponseKakaoMemberDto(
    val id: String,
    val kakaoId: Long,
    val nickname: String,
    val phoneNumber: String,
    val email: String,
    var token: String = "",
) {
    companion object {
        fun of(memberEntity: Member): ResponseKakaoMemberDto =
            ResponseKakaoMemberDto(
                id = memberEntity.id,
                kakaoId = memberEntity.kakaoId,
                nickname = memberEntity.name,
                phoneNumber = memberEntity.phoneNumber,
                email = memberEntity.emailL,
            )
    }

    override fun toString(): String =
        "ResponseKakaoMemberDto(id='$id', kakaoId=$kakaoId, nickname='$nickname', phoneNumber='$phoneNumber', email='$email', token='$token')"
}
