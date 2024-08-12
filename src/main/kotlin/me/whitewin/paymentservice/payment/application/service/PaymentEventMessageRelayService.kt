package me.whitewin.paymentservice.payment.application.service

import me.whitewin.paymentservice.common.Logger
import me.whitewin.paymentservice.common.UseCase
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentEventMessageRelayUseCase
import me.whitewin.paymentservice.payment.application.port.out.DispatchEventMessagePort
import me.whitewin.paymentservice.payment.application.port.out.LoadPendingPaymentEventMessagePort
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

@UseCase
@Profile("dev")
class PaymentEventMessageRelayService(
    private val loadingPendingPaymentOutboxPort: LoadPendingPaymentEventMessagePort,
    private val dispatchEventMessagePort: DispatchEventMessagePort
): PaymentEventMessageRelayUseCase {

    private val scheduler = Schedulers.newSingle("message-relay")
    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    override fun relay() {
        loadingPendingPaymentOutboxPort.getPendingPaymentMessage()
            .map { dispatchEventMessagePort.dispatch(it) }
            .onErrorContinue { err, _ -> Logger.error("messageRelay", err.message ?: ("failed to relay message.$err"))}
            .subscribeOn(scheduler)
            .subscribe()

    }
}