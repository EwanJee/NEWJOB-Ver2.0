package web.remember.domain.payment.application

import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException
import web.remember.domain.payment.Payment
import web.remember.domain.payment.repository.PaymentRepository

@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
) : PaymentService {
    override fun create(
        itemId: String,
        price: Int,
        memberId: String,
        name: String,
        email: String,
        phoneNumber: String,
    ): Payment {
        val uuid =
            java.util.UUID
                .randomUUID()
                .toString()
        val payment =
            Payment(
                id = uuid,
                method = "CARD",
                price = price,
                productName = itemId,
                memberId = memberId,
                name = name,
                email = email,
                phoneNumber = phoneNumber,
            )
        return paymentRepository.save(payment)
    }

    override fun findPriceById(id: String): Int {
        val payment = paymentRepository.findById(id).orElseThrow { CustomException("결제 정보를 찾을 수 없습니다.") }
        return payment.price
    }
}
