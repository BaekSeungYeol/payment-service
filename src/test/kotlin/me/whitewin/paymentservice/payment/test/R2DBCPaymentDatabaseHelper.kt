package me.whitewin.paymentservice.payment.test

import me.whitewin.paymentservice.payment.domain.PaymentEvent
import me.whitewin.paymentservice.payment.domain.PaymentOrder
import me.whitewin.paymentservice.payment.domain.PaymentStatus
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import java.math.BigDecimal


class R2DBCPaymentDatabaseHelper(
    private val databaseClient: DatabaseClient,
    private val transactionalOperator: TransactionalOperator
): PaymentDatabaseHelper {

    override fun getPayments(orderId: String): PaymentEvent? {
        return databaseClient.sql(SELECT_PAYMENT_QUERY)
            .bind("orderId", orderId)
            .fetch()
            .all()
            .groupBy { it["payment_event_id"] as Long }
            .flatMap { groupedFlux ->
                groupedFlux.collectList().map { results ->
                    PaymentEvent(
                        id = groupedFlux.key(),
                        orderId = results.first()["order_id"] as String,
                        orderName = results.first()["order_name"] as String,
                        buyerId = results.first()["buyer_id"] as Long,
                        isPaymentDone = ((results.first()["is_payment_done"] as Byte).toInt() == 1),
                        paymentOrders = results.map { result ->
                            PaymentOrder(
                                id = result["id"] as Long,
                                paymentEventId = groupedFlux.key(),
                                sellerId = result["seller_id"] as Long,
                                orderId = result["order_id"] as String,
                                productId = result["product_id"] as Long,
                                amount = result["amount"] as BigDecimal,
                                paymentStatus = PaymentStatus.get(result["payment_order_status"] as String),
                                isLedgerUpdated = ((result["ledger_updated"]) as Byte).toInt() == 1,
                                isWalletUpdated = ((result["wallet_updated"]) as Byte).toInt() == 1,
                            )
                        }
                    )
                }
            }
            .blockFirst()
    }

    override fun clean(): Mono<Void> {
        return deletePaymentOrders()
            .flatMap { deletePaymentEvent() }
            .`as`(transactionalOperator::transactional)
            .then()
    }

    private fun deletePaymentEvent(): Mono<Long> = databaseClient.sql(DELETE_PAYMENT_EVENT_QUERY)
        .fetch()
        .rowsUpdated()

    private fun deletePaymentOrders(): Mono<Long> {
        return databaseClient.sql(DELETE_PAYMENT_ORDER_QUERY)
            .fetch()
            .rowsUpdated()
    }

    companion object {
        val SELECT_PAYMENT_QUERY = """
            SELECT * from payment_events pe
            INNER JOIN payment_orders po ON po.order_id = pe.order_id
            WHERE pe.order_id = :orderId
        """.trimIndent()

        val DELETE_PAYMENT_EVENT_QUERY = """
            DELETE FROM payment_events
        """.trimIndent()

        val DELETE_PAYMENT_ORDER_QUERY = """
            DELETE FROM payment_orders
        """.trimIndent()

    }
}