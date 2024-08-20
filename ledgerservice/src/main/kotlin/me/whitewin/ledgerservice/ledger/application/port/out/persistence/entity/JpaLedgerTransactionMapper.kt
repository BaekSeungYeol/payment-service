package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import me.whitewin.ledgerservice.common.IdempotencyCreator
import me.whitewin.ledgerservice.ledger.domain.LedgerTransaction
import org.springframework.stereotype.Component

@Component
class JpaLedgerTransactionMapper {

    fun mapToJpaEntity(ledgerTransaction: LedgerTransaction): JpaLedgerTransactionEntity {
        return JpaLedgerTransactionEntity(
            description = "LedgerService record transaction",
            referenceType = ledgerTransaction.referenceType.name,
            referenceId = ledgerTransaction.referenceId,
            orderId = ledgerTransaction.orderId,
            idempotencyKey = IdempotencyCreator.create(ledgerTransaction)
        )
    }
}