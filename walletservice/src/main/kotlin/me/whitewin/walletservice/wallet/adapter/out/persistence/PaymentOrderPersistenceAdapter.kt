package me.whitewin.walletservice.wallet.adapter.out.persistence

import me.whitewin.walletservice.common.PersistentAdapter
import me.whitewin.walletservice.wallet.adapter.out.persistence.repository.PaymentOrderRepository
import me.whitewin.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import me.whitewin.walletservice.wallet.domain.PaymentOrder

@PersistentAdapter
class PaymentOrderPersistenceAdapter(
    private val paymentOrderRepository: PaymentOrderRepository
): LoadPaymentOrderPort {

    override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
        return paymentOrderRepository.getPaymentOrders(orderId)
    }
}