package me.whitewin.paymentservice.payment.adapter.`in`.stream

import me.whitewin.paymentservice.common.StreamAdapter
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentCompleteUseCase
import me.whitewin.paymentservice.payment.domain.LedgerEventMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import java.util.function.Function

@Configuration
@StreamAdapter
class LedgerEventMessageHandler(
    private val paymentCompleteUseCase: PaymentCompleteUseCase
) {

    @Bean
    fun ledger(): Function<Flux<Message<LedgerEventMessage>>, Mono<Void>> {
        return Function { flux ->
            flux.flatMap { message ->
                paymentCompleteUseCase.completePayment(message.payload)
                    .then(Mono.defer {
                        message.headers.get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset::class.java)!!.commit()
                    })
            }.then()
        }
    }

}