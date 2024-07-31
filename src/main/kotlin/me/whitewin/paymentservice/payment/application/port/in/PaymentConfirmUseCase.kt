package me.whitewin.paymentservice.payment.application.port.`in`

import me.whitewin.paymentservice.payment.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

interface PaymentConfirmUseCase {

    fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult>
}