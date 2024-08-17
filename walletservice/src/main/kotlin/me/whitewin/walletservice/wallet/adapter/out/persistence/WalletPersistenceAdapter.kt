package me.whitewin.walletservice.wallet.adapter.out.persistence

import me.whitewin.walletservice.common.PersistentAdapter
import me.whitewin.walletservice.wallet.adapter.out.persistence.repository.WalletRepository
import me.whitewin.walletservice.wallet.adapter.out.persistence.repository.WalletTransactionRepository
import me.whitewin.walletservice.wallet.application.port.out.DuplicateMessageFilterPort
import me.whitewin.walletservice.wallet.application.port.out.LoadWalletPort
import me.whitewin.walletservice.wallet.application.port.out.SaveWalletPort
import me.whitewin.walletservice.wallet.domain.PaymentEventMessage
import me.whitewin.walletservice.wallet.domain.Wallet

@PersistentAdapter
class WalletPersistenceAdapter(
    private val walletTransactionRepository: WalletTransactionRepository,
    private val walletRepository: WalletRepository
): DuplicateMessageFilterPort, LoadWalletPort, SaveWalletPort {

    override fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean {
        return walletTransactionRepository.isExist(paymentEventMessage)
    }

    override fun getWallets(sellerIds: Set<Long>): Set<Wallet> {
        return walletRepository.getWallets(sellerIds)
    }

    override fun save(wallets: List<Wallet>) {
        walletRepository.save(wallets)
    }
}