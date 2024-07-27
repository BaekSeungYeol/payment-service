package me.whitewin.paymentservice.payment.adapter.out.persistent

import me.whitewin.paymentservice.common.PersistentAdapter
import me.whitewin.paymentservice.payment.adapter.out.persistent.repository.PaymentRepository
import me.whitewin.paymentservice.payment.application.port.out.SavePaymentPort
import me.whitewin.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository
): SavePaymentPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }
}