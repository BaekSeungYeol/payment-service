package me.whitewin.paymentservice.payment.domain

data class CheckoutResult(
    val amount: Long,
    val orderId: String,
    val orderName: String
)