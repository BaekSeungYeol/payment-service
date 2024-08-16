package me.whitewin.walletservice.wallet.application.port.out

import me.whitewin.walletservice.wallet.domain.Wallet

interface SaveWalletPort {

    fun save(wallets: List<Wallet>)
}