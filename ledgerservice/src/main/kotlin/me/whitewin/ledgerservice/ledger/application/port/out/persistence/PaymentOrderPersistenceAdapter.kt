package me.whitewin.ledgerservice.ledger.application.port.out.persistence

import me.whitewin.ledgerservice.common.PersistentAdapter
import me.whitewin.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.PaymentOrderRepository
import me.whitewin.ledgerservice.ledger.domain.PaymentOrder

@PersistentAdapter
class PaymentOrderPersistenceAdapter(
    private val paymentOrderRepository: PaymentOrderRepository
): LoadPaymentOrderPort {

    override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
        return paymentOrderRepository.getPaymentOrders(orderId)
    }
}