package me.whitewin.walletservice.wallet.application.port.out

import me.whitewin.walletservice.wallet.domain.PaymentOrder

interface LoadPaymentOrderPort {

    fun getPaymentOrders(orderId: String): List<PaymentOrder>
}