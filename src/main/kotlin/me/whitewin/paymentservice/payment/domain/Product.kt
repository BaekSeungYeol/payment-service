package me.whitewin.paymentservice.payment.domain

import java.math.BigDecimal

class Product(
    val id: Long,
    val amount: Long,
    val quantity: Int,
    val name: String,
    val sellerId: Long
)