package me.whitewin.paymentservice.payment.application.service

import me.whitewin.paymentservice.common.UseCase
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmUseCase
import me.whitewin.paymentservice.payment.application.port.out.PaymentExecutorPort
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import me.whitewin.paymentservice.payment.application.port.out.PaymentValidationPort
import me.whitewin.paymentservice.payment.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService(
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    private val paymentValidationPort: PaymentValidationPort,
    private val paymentExecutorPort: PaymentExecutorPort,
    private val paymentErrorHandler: PaymentErrorHandler
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
            .onErrorResume { error -> paymentErrorHandler.handlePaymentConfirmationError(error, command) }

    }
}