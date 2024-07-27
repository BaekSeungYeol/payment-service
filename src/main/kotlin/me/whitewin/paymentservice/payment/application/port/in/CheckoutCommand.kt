package me.whitewin.paymentservice.payment.application.port.`in`

data class CheckoutCommand(
    val cartId: Long,
    val buyerId: Long,
    val productIds: List<Long>,
    val idempotencyKey: String
)