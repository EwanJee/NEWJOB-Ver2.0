package web.remember.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import web.remember.domain.payment.Payment

@Repository
interface PaymentRepository : JpaRepository<Payment, String> {
    fun findByEmail(email: String): List<Payment>

    fun findByName(name: String): List<Payment>

    fun findByPhoneNumber(phoneNumber: String): List<Payment>

    fun findByProductName(productName: String): List<Payment>
}
