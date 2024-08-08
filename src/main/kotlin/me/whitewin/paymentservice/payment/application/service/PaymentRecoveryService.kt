package me.whitewin.paymentservice.payment.application.service

import me.whitewin.paymentservice.common.UseCase
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentRecoveryUseCase
import me.whitewin.paymentservice.payment.application.port.out.*
import mu.KLogger
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

@UseCase
class PaymentRecoveryService(
    private val loadPendingPaymentPort: LoadPendingPaymentPort,
    private val paymentValidationPort: PaymentValidationPort,
    private val paymentExecutorPort: PaymentExecutorPort,
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
) :  PaymentRecoveryUseCase{

    private val scheduler = Schedulers.newSingle("recovery")

    @Scheduled(fixedDelay = 180, timeUnit = TimeUnit.SECONDS)
    override fun recovery() {
        loadPendingPaymentPort.getPendingPayments()
            .map {
                PaymentConfirmCommand(
                    paymentKey = it.paymentKey,
                    orderId = it.orderId,
                    amount = it.totalAmount()

                )
            }
            .parallel(2)
            .runOn(Schedulers.parallel())
            .flatMap { paymentValidationPort.isValid(it.orderId, it.amount).thenReturn(it) }
            .flatMap { paymentExecutorPort.execute(it) }
            .flatMap { paymentStatusUpdatePort.updatePaymentStatus(PaymentStatusUpdateCommand(it)).thenReturn(it)}
            .sequential()
            .doOnEach {
                when(it.hasError() && it.isOnComplete.not()) {
                    true -> logger.info { "recovery failure, orderId: ${it.get()?.orderId}" }
                    false -> logger.info { "recovery success, orderId: ${it.get()?.orderId}" }

                }
            }
            .subscribeOn(scheduler)
            .subscribe()
    }

    companion object: KLogging()
}