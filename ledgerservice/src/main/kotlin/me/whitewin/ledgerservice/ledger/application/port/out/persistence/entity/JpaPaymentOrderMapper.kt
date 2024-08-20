package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import me.whitewin.ledgerservice.ledger.domain.PaymentOrder
import org.springframework.stereotype.Component

@Component
class JpaPaymentOrderMapper {

    fun mapToDomainEntity(jpaPaymentOrderEntity: JpaPaymentOrderEntity): PaymentOrder {
        return PaymentOrder(
            id = jpaPaymentOrderEntity.id!!,
            amount = jpaPaymentOrderEntity.amount.toLong(),
            orderId = jpaPaymentOrderEntity.orderId
        )
    }
}