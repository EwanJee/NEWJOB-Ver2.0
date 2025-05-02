@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private var secretKey: String,
) {
    companion object {
        private const val EXPIRATION_TIME = 60 * 60 * 1000 * 4 // 4시간
    }

    fun generateToken(
        subject: String,
        claims: Map<String, Any>,
    ): String {
        val signingKey: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts
            .builder()
            .setSubject(subject)
            .setClaims(claims)
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createAccessToken(
        email: String,
        role: String,
    ): String =
        Jwts
            .builder()
            .setSubject(email)
            .claim("role", role)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1시간
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName))
            .compact()
}
