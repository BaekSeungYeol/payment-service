package me.whitewin.paymentservice.payment.adapter.out.persistent.repository

import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import me.whitewin.paymentservice.payment.domain.PaymentEventMessage
import me.whitewin.paymentservice.payment.domain.PaymentEventMessageType
import reactor.core.publisher.Mono

interface PaymentOutboxRepository {

    fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage>
    fun markMessageAsSent(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>
    fun markMessageAsFailure(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>


}