package web.remember.domain.payment.application

import web.remember.domain.payment.Payment

interface PaymentService {
    fun create(
        itemId: String,
        price: Int,
        memberId: String,
        name: String,
        email: String,
        phoneNumber: String,
    ): Payment

    fun findPriceById(id: String): Int
}
