package me.whitewin.paymentservice.payment.adapter.out.persistent.repository

import me.whitewin.paymentservice.common.objectMapper
import me.whitewin.paymentservice.payment.adapter.out.stream.util.PartitionKeyUtil
import me.whitewin.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import me.whitewin.paymentservice.payment.domain.PaymentEventMessage
import me.whitewin.paymentservice.payment.domain.PaymentEventMessageType
import me.whitewin.paymentservice.payment.domain.PaymentStatus
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2DBCPaymentOutboxRepository(
    private val databaseClient: DatabaseClient,
    private val partitionKeyUtil: PartitionKeyUtil
): PaymentOutboxRepository {

    override fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage> {
        require(command.status == PaymentStatus.SUCCESS)

        val paymentEventMessage = createPaymentEventMessage(command)

        return databaseClient.sql(INSERT_OUTBOX_QUERY)
            .bind("idempotencyKey", paymentEventMessage.payload["orderId"]!!)
            .bind("partitionKey", paymentEventMessage.metadata["partitionKey"] ?: 0)
            .bind("type", paymentEventMessage.type.name)
            .bind("payload", objectMapper.writeValueAsString(paymentEventMessage.payload))
            .bind("metadata", objectMapper.writeValueAsString(paymentEventMessage.metadata))
            .fetch()
            .rowsUpdated()
            .thenReturn(paymentEventMessage)
    }

    override fun markMessageAsSent(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean> {
        return databaseClient.sql(UPDATE_OUTBOX_MESSAGE_AS_SENT_QUERY)
            .bind("idempotencyKey", idempotencyKey)
            .bind("type", type.name)
            .fetch()
            .rowsUpdated()
            .thenReturn(true)
    }

    override fun markMessageAsFailure(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean> {
        return databaseClient.sql(UPDATE_OUTBOX_MESSAGE_AS_FAILURE_QUERY)
            .bind("idempotencyKey", idempotencyKey)
            .bind("type", type.name)
            .fetch()
            .rowsUpdated()
            .thenReturn(true)
    }

    private fun createPaymentEventMessage(command: PaymentStatusUpdateCommand): PaymentEventMessage {
        return PaymentEventMessage(
            type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
            payload = mapOf(
                "orderId" to command.orderId
            ),
            metadata = mapOf(
                "partitionKey" to partitionKeyUtil.createPartitionKey(command.orderId.hashCode())
            )
        )
    }

    companion object {
        val INSERT_OUTBOX_QUERY = """
            INSERT INTO outboxes (idempotency_key, type, partition_key, payload, metadata)
            VALUES (:idempotency_key, :type, :partition_key, :payload, :metadata);
        """.trimIndent()

        val UPDATE_OUTBOX_MESSAGE_AS_SENT_QUERY = """
            UPDATE outboxes
            SET status = 'SUCCESS'
            WHERE idempotency_key = :idempotencyKey
            AND type =
        """.trimIndent()

        val UPDATE_OUTBOX_MESSAGE_AS_FAILURE_QUERY = """
            UPDATE outboxes
            SET status = 'FAILURE'
            WHERE idempotency_key = :idempotencyKey
            AND type =
        """.trimIndent()
    }
}