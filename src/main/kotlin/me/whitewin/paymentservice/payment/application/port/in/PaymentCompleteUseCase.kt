package me.whitewin.paymentservice.payment.application.port.`in`

import me.whitewin.paymentservice.payment.domain.LedgerEventMessage
import me.whitewin.paymentservice.payment.domain.WalletEventMessage
import reactor.core.publisher.Mono

interface PaymentCompleteUseCase {

    fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void>

    fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void>
}