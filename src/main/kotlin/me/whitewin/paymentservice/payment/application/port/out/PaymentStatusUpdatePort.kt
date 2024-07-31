package me.whitewin.paymentservice.payment.application.port.out

import me.whitewin.paymentservice.payment.domain.PaymentStatus
import reactor.core.publisher.Mono

interface PaymentStatusUpdatePort {

    fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}