package me.whitewin.paymentservice.payment.application.service

import io.netty.handler.timeout.TimeoutException
import me.whitewin.paymentservice.common.UseCase
import me.whitewin.paymentservice.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException
import me.whitewin.paymentservice.payment.adapter.out.persistent.exception.PaymentValidationException
import me.whitewin.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmUseCase
import me.whitewin.paymentservice.payment.application.port.out.PaymentExecutorPort
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import me.whitewin.paymentservice.payment.application.port.out.PaymentValidationPort
import me.whitewin.paymentservice.payment.domain.PaymentConfirmationResult
import me.whitewin.paymentservice.payment.domain.PaymentFailure
import me.whitewin.paymentservice.payment.domain.PaymentStatus
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService(
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    private val paymentValidationPort: PaymentValidationPort,
    private val paymentExecutorPort: PaymentExecutorPort
) : PaymentConfirmUseCase {

    override fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
        return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId, command.paymentKey)
            .filterWhen { paymentValidationPort.isValid(command.orderId, command.amount) }
            .flatMap { paymentExecutorPort.execute(command) }
            .flatMap {
                paymentStatusUpdatePort.updatePaymentStatus(
                    command = PaymentStatusUpdateCommand(
                        paymentKey = it.paymentKey,
                        orderId = it.orderId,
                        status = it.paymentStatus(),
                        extraDetails = it.extraDetails,
                        failure = it.failure
                    )
                ).thenReturn(it)
            }
            .map { PaymentConfirmationResult(status = it.paymentStatus(), failure = it.failure) }
            .onErrorResume { error -> handlePaymentError(error, command) }

    }

    private fun handlePaymentError(
        error: Throwable,
        command: PaymentConfirmCommand
    ): Mono<PaymentConfirmationResult> {
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