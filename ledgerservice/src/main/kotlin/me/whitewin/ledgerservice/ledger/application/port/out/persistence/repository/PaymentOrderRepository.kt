package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.domain.PaymentOrder

interface PaymentOrderRepository {

    fun getPaymentOrders(orderId: String): List<PaymentOrder>
}