package me.whitewin.paymentservice.payment.application.service

import io.netty.handler.timeout.TimeoutException
import me.whitewin.paymentservice.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException
import me.whitewin.paymentservice.payment.adapter.out.persistent.exception.PaymentValidationException
import me.whitewin.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import me.whitewin.paymentservice.payment.domain.PaymentConfirmationResult
import me.whitewin.paymentservice.payment.domain.PaymentFailure
import me.whitewin.paymentservice.payment.domain.PaymentStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentErrorHandler(
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort
) {

    fun handlePaymentConfirmationError(error: Throwable,command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
        val (status, failure) = when (error) {
            is PSPConfirmationException -> Pair(error.paymentStatus(), PaymentFailure(error.errorCode, error.errorMessage))
            is PaymentValidationException -> Pair(PaymentStatus.FAILURE, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
            is PaymentAlreadyProcessedException -> return Mono.just(PaymentConfirmationResult(status = error.status, failure = PaymentFailure(error::class.simpleName ?: "", error.message ?: "")))
            is TimeoutException -> Pair(PaymentStatus.UNKNOWN, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
            else -> Pair(PaymentStatus.UNKNOWN, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
        }

        val paymentStatusUpdateCommand = PaymentStatusUpdateCommand(
            paymentKey = command.paymentKey,
            orderId = command.orderId,
            status = status,
            failure = failure
        )

        return paymentStatusUpdatePort.updatePaymentStatus(paymentStatusUpdateCommand)
            .map { PaymentConfirmationResult(status, failure) }
    }
}