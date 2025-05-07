package web.remember.domain.payment.repository

interface CustomizedTestAndPaymentRepository {
    fun updateTestAndPaymentAfterPayment(
        testId: Long,
        paymentId: String,
    ): Boolean
}
