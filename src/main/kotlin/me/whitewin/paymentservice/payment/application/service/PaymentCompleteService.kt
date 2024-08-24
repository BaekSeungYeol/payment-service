package me.whitewin.paymentservice.payment.application.service

import me.whitewin.paymentservice.common.UseCase
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentCompleteUseCase
import me.whitewin.paymentservice.payment.application.port.out.CompletePaymentPort
import me.whitewin.paymentservice.payment.application.port.out.LoadPaymentPort
import me.whitewin.paymentservice.payment.domain.LedgerEventMessage
import me.whitewin.paymentservice.payment.domain.WalletEventMessage
import reactor.core.publisher.Mono

@UseCase
class PaymentCompleteService(
    private val loadPaymentPort: LoadPaymentPort,
    private val completePaymentPort: CompletePaymentPort
): PaymentCompleteUseCase {

    override fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void> {
        return loadPaymentPort.getPayment(walletEventMessage.orderId())
            .map { it.apply { confirmWalletUpdate() }}
            .map { it.apply { completeIfDone() }}
            .flatMap { completePaymentPort.complete(it) }
    }


    override fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void> {
        return loadPaymentPort.getPayment(ledgerEventMessage.orderId())
            .map { it.apply { confirmLedgerUpdate() }}
            .map { it.apply { completeIfDone() }}
            .flatMap { completePaymentPort.complete(it) }
    }
}