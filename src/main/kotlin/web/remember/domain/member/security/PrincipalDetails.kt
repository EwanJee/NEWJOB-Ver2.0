package web.remember.domain.member.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import web.remember.domain.member.entity.Member

class PrincipalDetails(
    private val member: Member,
    val attributesMap: Map<String, Any>,
) : UserDetails,
    OAuth2User {
    override fun getName(): String = member.name

    override fun getAttributes(): MutableMap<String, Any> = attributesMap.toMutableMap()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(GrantedAuthority { "ROLE_USER" })

    override fun getPassword(): String = ""

    override fun getUsername(): String = member.name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getMemberId(): String = member.id
}
