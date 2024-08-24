package me.whitewin.paymentservice.payment.application.port.out

import me.whitewin.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface CompletePaymentPort {

    fun complete(paymentEvent: PaymentEvent): Mono<Void>
}