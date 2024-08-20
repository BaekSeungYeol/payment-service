package me.whitewin.ledgerservice.ledger.application.port.out

import me.whitewin.ledgerservice.ledger.domain.PaymentOrder

interface LoadPaymentOrderPort {

    fun getPaymentOrders(orderId: String): List<PaymentOrder>
}