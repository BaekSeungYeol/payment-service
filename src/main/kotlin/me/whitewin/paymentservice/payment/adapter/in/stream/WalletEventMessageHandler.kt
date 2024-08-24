package me.whitewin.paymentservice.payment.adapter.`in`.stream

import me.whitewin.paymentservice.common.StreamAdapter
import me.whitewin.paymentservice.payment.application.port.`in`.PaymentCompleteUseCase
import me.whitewin.paymentservice.payment.domain.WalletEventMessage
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import java.util.function.Function

@Configuration
@StreamAdapter
class WalletEventMessageHandler(
    private val paymentCompleteUseCase: PaymentCompleteUseCase
) {

    fun wallet(): Function<Flux<Message<WalletEventMessage>>, Mono<Void>> {
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