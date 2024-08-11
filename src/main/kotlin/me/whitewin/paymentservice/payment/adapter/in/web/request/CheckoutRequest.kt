package me.whitewin.paymentservice.payment.adapter.`in`.web.request

import java.time.LocalDateTime

data class CheckoutRequest(
    val cartId: Long = 1,
    val productIds: List<Long> = listOf(1,2,3),
    val buyerId: Long = 1,
    /* 현재와 미래의 주문이 같을때*/ val seed: String = LocalDateTime.now().toString(),
)
