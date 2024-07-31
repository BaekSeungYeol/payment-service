package me.whitewin.paymentservice.payment.adapter.out.web.toss.executor

import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutor {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}