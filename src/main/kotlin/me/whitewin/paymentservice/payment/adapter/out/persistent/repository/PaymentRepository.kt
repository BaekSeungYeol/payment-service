package me.whitewin.paymentservice.payment.adapter.out.persistent.repository

import me.whitewin.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent): Mono<Void>
}