package web.remember.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "item")
class Item(
    @Id
    val id: String,
    val name: String,
    val price: Int,
    val currency: String = "KRW",
)
