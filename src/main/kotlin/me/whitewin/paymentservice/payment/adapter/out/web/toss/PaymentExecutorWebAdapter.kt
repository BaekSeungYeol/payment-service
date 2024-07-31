package me.whitewin.paymentservice.payment.adapter.out.web.toss

import me.whitewin.paymentservice.common.WebAdapter
import me.whitewin.paymentservice.payment.adapter.out.web.toss.executor.PaymentExecutor
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.application.port.out.PaymentExecutorPort
import me.whitewin.paymentservice.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

@WebAdapter
class PaymentExecutorWebAdapter(
    private val paymentExecutor: PaymentExecutor
) : PaymentExecutorPort {

    override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        return paymentExecutor.execute(command)
    }
}