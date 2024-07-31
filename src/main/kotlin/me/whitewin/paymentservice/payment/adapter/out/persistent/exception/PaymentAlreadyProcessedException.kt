package me.whitewin.paymentservice.payment.adapter.out.persistent.exception

import me.whitewin.paymentservice.payment.domain.PaymentStatus

class PaymentAlreadyProcessedException(
    val status: PaymentStatus,
    message: String
): RuntimeException(message)