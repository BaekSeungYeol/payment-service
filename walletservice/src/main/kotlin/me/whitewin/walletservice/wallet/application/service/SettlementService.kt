package me.whitewin.walletservice.wallet.application.service

import me.whitewin.walletservice.common.UseCase
import me.whitewin.walletservice.wallet.application.port.`in`.SettlementUseCase
import me.whitewin.walletservice.wallet.application.port.out.DuplicateMessageFilterPort
import me.whitewin.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import me.whitewin.walletservice.wallet.application.port.out.LoadWalletPort
import me.whitewin.walletservice.wallet.application.port.out.SaveWalletPort
import me.whitewin.walletservice.wallet.domain.*

@UseCase
class SettlementService(
    private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
    private val loadPaymentOrderPort: LoadPaymentOrderPort,
    private val loadWalletPort: LoadWalletPort,
    private val saveWalletPort: SaveWalletPort
) : SettlementUseCase {

    override fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage {
        if (duplicateMessageFilterPort.isAlreadyProcess(paymentEventMessage)) {
            return createWalletEventMessage(paymentEventMessage)
        }

        val paymentOrders = loadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId())
        val paymentOrdersBySellerId = paymentOrders.groupBy { it.sellerId }
        val updatedWallets = getUpdatedWallets(paymentOrdersBySellerId)

        saveWalletPort.save(updatedWallets)

        return createWalletEventMessage(paymentEventMessage)
    }

    private fun getUpdatedWallets(paymentOrdersBySellerId: Map<Long, List<PaymentOrder>>): List<Wallet> {
        val sellerIds = paymentOrdersBySellerId.keys

        val wallets = loadWalletPort.getWallets(sellerIds)

        return wallets.map { wallet ->
            wallet.calculateBalanceWith(paymentOrdersBySellerId[wallet.userId]!!)
        }
    }

    private fun createWalletEventMessage(paymentEventMessage: PaymentEventMessage) =
        WalletEventMessage(
            type = WalletEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to paymentEventMessage.orderId()
            )
        )
}