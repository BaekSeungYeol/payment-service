package me.whitewin.walletservice.wallet.adapter.out.persistence.repository

import me.whitewin.walletservice.wallet.domain.Wallet

interface WalletRepository {

    fun getWallets(sellerIds: Set<Long>): Set<Wallet>
    fun save(wallets: List<Wallet>)
}