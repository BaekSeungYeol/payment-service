package me.whitewin.paymentservice.payment.application.port.`in`

import me.whitewin.paymentservice.payment.domain.CheckoutResult
import reactor.core.publisher.Mono

interface CheckoutUseCase {

    fun checkout(command: CheckoutCommand): Mono<CheckoutResult>
}