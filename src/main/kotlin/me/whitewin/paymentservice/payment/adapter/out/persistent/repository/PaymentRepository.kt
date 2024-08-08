package me.whitewin.paymentservice.payment.adapter.out.persistent.repository

import me.whitewin.paymentservice.payment.domain.PaymentEvent
import me.whitewin.paymentservice.payment.domain.PendingPaymentEvent
import me.whitewin.paymentservice.payment.domain.PendingPaymentOrder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent): Mono<Void>

    fun getPendingPayments(): Flux<PendingPaymentEvent>
}