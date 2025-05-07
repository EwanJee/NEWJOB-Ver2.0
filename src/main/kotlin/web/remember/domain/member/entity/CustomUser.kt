package web.remember.domain.member.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val id: String,
    username: String,
    val email: String,
    val phoneNumber: String,
    val industry: String,
    val kakaoId: Long,
    authorities: Collection<GrantedAuthority>,
) : User(username, "", authorities)
