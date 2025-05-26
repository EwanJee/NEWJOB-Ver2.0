@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.member.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "member")
@EntityListeners(AuditingEntityListener::class)
@Entity
class Member(
    name: String,
    phoneNumber: String = "",
    industry: String = "",
) {
    @Id
    @Column(name = "id")
    val id: String = UUID.randomUUID().toString()

    @Column(name = "kakao_id")
    var kakaoId: Long = 0L

    @Column(name = "name")
    var name: String = name

    @Column(name = "phone_number")
    var phoneNumber: String = convert(phoneNumber)

    @Column(name = "email")
    var emailL: String = ""

    @Column(name = "industry")
    var industry: String = industry

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    lateinit var createdAt: LocalDateTime

    private fun convert(phoneNumber: String): String {
        val converted =
            phoneNumber
                .trim()
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace(" ", "")
        return if (converted.startsWith("010")) {
            converted
        } else {
            "010$converted"
        }
    }
}
