package web.remember.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "payment")
class Payment(
    @Id
    val id: String,
    val method: String,
    val productName: String,
    val memberId: String,
    val price: Int,
    val email: String,
    val name: String,
    val phoneNumber: String,
    var status: String = "READY",
)
