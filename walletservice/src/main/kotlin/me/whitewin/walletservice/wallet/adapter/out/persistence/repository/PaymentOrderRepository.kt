package me.whitewin.walletservice.wallet.adapter.out.persistence.repository

import me.whitewin.walletservice.wallet.domain.PaymentOrder

interface PaymentOrderRepository {

    fun getPaymentOrders(orderId: String): List<PaymentOrder>
}