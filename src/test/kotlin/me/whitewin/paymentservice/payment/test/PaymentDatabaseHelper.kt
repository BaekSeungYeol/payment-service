package me.whitewin.paymentservice.payment.test

import me.whitewin.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {

    fun getPayments(orderId: String): PaymentEvent?

    fun clean(): Mono<Void>
}