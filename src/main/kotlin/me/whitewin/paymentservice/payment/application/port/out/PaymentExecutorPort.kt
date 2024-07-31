package me.whitewin.paymentservice.payment.application.port.out

import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutorPort {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}