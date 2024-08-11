package me.whitewin.paymentservice.payment.adapter.out.stream

import jakarta.annotation.PostConstruct
import me.whitewin.paymentservice.common.Logger
import me.whitewin.paymentservice.common.StreamAdapter
import me.whitewin.paymentservice.payment.adapter.out.persistent.repository.PaymentOutboxRepository
import me.whitewin.paymentservice.payment.domain.PaymentEventMessage
import me.whitewin.paymentservice.payment.domain.PaymentEventMessageType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.IntegrationMessageHeaderAccessor
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.FluxMessageChannel
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.kafka.sender.SenderResult
import java.util.function.Supplier

@Configuration
@StreamAdapter
class PaymentEventMessageSender(
    private val paymentOutboxRepository: PaymentOutboxRepository
) {

    /**
     * data stream 을 동적으로 생성하고 발출하는 메커니즘
     */
    private val sender = Sinks.many().unicast().onBackpressureBuffer<Message<PaymentEventMessage>>()
    private val sendResult = Sinks.many().unicast().onBackpressureBuffer<SenderResult<String>>()

    @Bean
    fun send(): Supplier<Flux<Message<PaymentEventMessage>>> {
        return Supplier {
            sender.asFlux()
                .onErrorContinue { err, _ ->
                    Logger.error("sendEventMessage", err.message ?: "failed to send eventMessage", err)
                }
        }
    }

    @Bean(name = ["payment-result"])
    fun sendResultChannel(): FluxMessageChannel {
        return FluxMessageChannel()
    }

    @ServiceActivator(inputChannel = "payment-result")
    fun receiveSendResult(results: SenderResult<String>) {
        if(results.exception() != null) {
            Logger.error("sendEventMessage", results.exception().message ?: "receive an exception for event message send.", results.exception())
        }

        sendResult.emitNext(results, Sinks.EmitFailureHandler.FAIL_FAST)
    }

    @PostConstruct
    fun handleSendResult() {
        sendResult.asFlux()
            .flatMap {
                when(it.recordMetadata() != null) {
                    true -> paymentOutboxRepository.markMessageAsSent(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
                    false -> paymentOutboxRepository.markMessageAsFailure(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
                }
            }
            .onErrorContinue { err, _ -> Logger.error("sendEventMessage", err.message ?: "failed to mark the outbox message", err) }
            .subscribeOn(Schedulers.newSingle("receive-send-result-event-message"))
            .subscribe()
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun dispatchAfterCommit(paymentEventMessage: PaymentEventMessage) {
        dispatch(paymentEventMessage)
    }

    fun dispatch(paymentEventMessage: PaymentEventMessage) {
        sender.emitNext(createEventMessage(paymentEventMessage), Sinks.EmitFailureHandler.FAIL_FAST)
    }

    private fun createEventMessage(paymentEventMessage: PaymentEventMessage): Message<PaymentEventMessage> {
        return MessageBuilder.withPayload(paymentEventMessage)
            .setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID, paymentEventMessage.payload["orderId"])
            .setHeader(KafkaHeaders.PARTITION, paymentEventMessage.metadata["partitionKey"] ?: 0)
            .build()
    }

}