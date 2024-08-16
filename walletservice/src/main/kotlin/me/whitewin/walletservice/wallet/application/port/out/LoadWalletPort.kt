package me.whitewin.walletservice.wallet.application.port.out

import me.whitewin.walletservice.wallet.domain.Wallet

interface LoadWalletPort {

    fun getWallets(sellerIds: Set<Long>): Set<Wallet>
}