package me.whitewin.walletservice.wallet.adapter.out.persistence.repository

import me.whitewin.walletservice.wallet.domain.PaymentEventMessage
import me.whitewin.walletservice.wallet.domain.WalletTransaction

interface WalletTransactionRepository {

    fun isExist(paymentEventMessage: PaymentEventMessage): Boolean

    fun save(walletTransactions: List<WalletTransaction>)
}