package web.remember.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Table(name = "member")
@Entity
class Member(
    name: String,
    phoneNumber: String,
) {
    @Id
    @Column(name = "id")
    val id: String = UUID.randomUUID().toString()

    @Column(name = "name")
    var name: String = name

    @Column(name = "phone_number")
    var phoneNumber: String = convert(phoneNumber)

    private fun convert(phoneNumber: String): String =
        phoneNumber
            .trim()
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")
}
