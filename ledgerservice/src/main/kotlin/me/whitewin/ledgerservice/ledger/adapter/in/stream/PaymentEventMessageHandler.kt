package me.whitewin.ledgerservice.ledger.adapter.`in`.stream

import me.whitewin.ledgerservice.common.StreamAdapter
import me.whitewin.ledgerservice.ledger.application.port.`in`.DoubleLedgerEntryRecordUseCase
import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
@StreamAdapter
class PaymentEventMessageHandler(
    private val doubleLedgerEntryRecordUseCase: DoubleLedgerEntryRecordUseCase,
    private val streamBridge: StreamBridge
) {

    @Bean
    fun consume(): Consumer<Message<PaymentEventMessage>> {
        return Consumer { message ->
            val ledgerEventMessage = doubleLedgerEntryRecordUseCase.recordDoubleLedgerEntry(message.payload)
            streamBridge.send("ledger", ledgerEventMessage)
        }
    }
}