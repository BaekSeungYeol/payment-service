package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import me.whitewin.ledgerservice.ledger.domain.DoubleLedgerEntry
import org.springframework.stereotype.Component

@Component
class JpaLedgerEntryMapper(
    private val jpaLedgerTransactionMapper: JpaLedgerTransactionMapper
) {

    fun mapToJpaEntity(doubleLedgerEntry: DoubleLedgerEntry): List<JpaLedgeEntryEntity> {
        val jpaLedgerTransactionEntity = jpaLedgerTransactionMapper.mapToJpaEntity(doubleLedgerEntry.transaction)
        return listOf(
            JpaLedgeEntryEntity(
                amount = doubleLedgerEntry.credit.amount.toBigDecimal(),
                accountId = doubleLedgerEntry.credit.account.id,
                type = doubleLedgerEntry.credit.type,
                transaction = jpaLedgerTransactionEntity
            ),
            JpaLedgeEntryEntity(
                amount = doubleLedgerEntry.credit.amount.toBigDecimal(),
                accountId = doubleLedgerEntry.credit.account.id,
                type = doubleLedgerEntry.debit.type,
                transaction = jpaLedgerTransactionEntity
            )
        )
    }
}