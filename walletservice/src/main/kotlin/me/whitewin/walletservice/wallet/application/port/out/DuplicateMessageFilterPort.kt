package me.whitewin.walletservice.wallet.application.port.out

import me.whitewin.walletservice.wallet.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

    fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean
}