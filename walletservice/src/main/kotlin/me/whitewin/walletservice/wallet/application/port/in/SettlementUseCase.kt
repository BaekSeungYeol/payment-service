package me.whitewin.walletservice.wallet.application.port.`in`

import me.whitewin.walletservice.wallet.domain.PaymentEventMessage
import me.whitewin.walletservice.wallet.domain.WalletEventMessage

interface SettlementUseCase {

    fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage
}