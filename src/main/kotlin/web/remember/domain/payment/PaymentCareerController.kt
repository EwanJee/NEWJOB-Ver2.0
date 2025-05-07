@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.payment

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import web.remember.domain.error.CustomException
import web.remember.domain.member.application.MemberService
import web.remember.domain.member.dto.MemberInfoDto
import web.remember.domain.payment.application.ItemService
import web.remember.domain.payment.application.PaymentService
import web.remember.domain.payment.repository.CustomizedTestAndPaymentRepository
import web.remember.domain.test.application.TestService
import web.remember.util.JwtUtil
import java.net.URLEncoder

@RestController
@RequestMapping("api/v1/payment/career")
class PaymentCareerController(
    private val itemService: ItemService,
    private val memberService: MemberService,
    private val paymentService: PaymentService,
    private val jwtUtil: JwtUtil,
    private val testService: TestService,
    private val webClientBuilder: WebClient.Builder,
    private val testAndPaymentRepository: CustomizedTestAndPaymentRepository,
) {
    @Value("\${channel.key}")
    private lateinit var channelKey: String

    @Value("\${store.key}")
    private lateinit var storeKey: String

    @Value("\${payment.key}")
    private lateinit var portOneSecret: String

    @GetMapping()
    fun createPayment(
        @CookieValue("jwt") jwt: String,
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<Map<String, Any>> {
        if (user.authorities.isEmpty()) {
            throw CustomException("권한이 없습니다.")
        }
        val claims = jwtUtil.parseClaims(jwt)
        val memberId = claims["memberId"] as String
        val testId = claims["testId"] as String
        if (testService.isValidForPayment(testId.toLong(), memberId)) {
            throw CustomException("결제할 수 없는 테스트입니다.")
        }
        val member: MemberInfoDto = memberService.findById(memberId)
        val item: Item = itemService.getItemById("CAREER")
        val payment =
            paymentService.create(item.id, item.price, memberId, member.name, member.email, member.phoneNumber)
        val response =
            mapOf(
                "storeId" to storeKey,
                "channelKey" to channelKey,
                "payMethod" to payment.method,
                "paymentId" to "payment-${payment.id}",
                "orderName" to payment.productName,
                "totalAmount" to payment.price,
                "currency" to "CURRENCY_KRW",
                "memberId" to memberId,
                "testId" to testId,
            )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/complete")
    fun completePayment(
        @RequestBody body: Map<String, String>,
        @CookieValue("jwt") jwt: String,
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<String> {
        if (user.authorities.isEmpty()) {
            throw CustomException("권한이 없습니다.")
        }

        val claims = jwtUtil.parseClaims(jwt)
        val memberId1 = claims["memberId"] as String
        val testId1 = claims["testId"] as String

        val paymentId = body["paymentId"] ?: throw CustomException("paymentId가 누락되었습니다.")
        val filteredPaymentId = paymentId.substringAfter("payment-")
        val priceComp = paymentService.findPriceById(filteredPaymentId)

        if (!checkWebClientResponse(paymentId, priceComp)) {
            throw CustomException("결제 정보가 유효하지 않습니다.")
        }

        val isUpdated =
            testAndPaymentRepository.updateTestAndPaymentAfterPayment(
                testId = testId1.toLong(),
                paymentId = filteredPaymentId,
            )
        if (!isUpdated) {
            throw CustomException("결제 정보를 업데이트하는데 실패했습니다.")
        }

        return ResponseEntity.ok(paymentId)
    }

    private fun checkWebClientResponse(
        paymentId: String,
        priceComp: Int,
    ): Boolean {
        val paymentResponse: PaymentResponse? =
            webClientBuilder
                .build()
                .get()
                .uri("https://api.portone.io/payments/${URLEncoder.encode(paymentId, "UTF-8")}")
                .header(HttpHeaders.AUTHORIZATION, "PortOne $portOneSecret")
                .retrieve()
                .onStatus({ !it.is2xxSuccessful }) {
                    it.bodyToMono(String::class.java).map { body ->
                        RuntimeException("paymentResponse: $body")
                    }
                }.bodyToMono(PaymentResponse::class.java)
                .block()
        if (paymentResponse == null) {
            throw CustomException("결제 정보를 찾을 수 없습니다.")
        }
        if (paymentResponse.status != "PAID") {
            throw CustomException("결제 상태가 유효하지 않습니다.")
        }
        if (paymentResponse.amount.total != priceComp) {
            throw CustomException("결제 금액이 일치하지 않습니다.")
        }
        return true
    }
}

data class PaymentResponse(
    val amount: Amount,
    val status: String,
    val paymentMethod: String? = null,
)

data class Amount(
    val total: Int,
)
