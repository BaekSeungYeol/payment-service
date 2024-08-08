package me.whitewin.paymentservice.payment.application.port.out

import me.whitewin.paymentservice.payment.domain.PendingPaymentEvent
import reactor.core.publisher.Flux

interface LoadPendingPaymentPort {

    fun getPendingPayments(): Flux<PendingPaymentEvent>
}