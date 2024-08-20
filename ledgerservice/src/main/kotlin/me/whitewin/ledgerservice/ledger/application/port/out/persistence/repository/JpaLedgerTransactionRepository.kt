package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity.JpaLedgerTransactionEntity
import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaLedgerTransactionRepository(
    private val springDataJpaLedgerTransactionRepository: SpringDataJpaLedgerTransactionRepository
): LedgerTransactionRepository {

    override fun isExist(message: PaymentEventMessage): Boolean {
        return springDataJpaLedgerTransactionRepository.existsByOrderId(message.orderId())
    }
}

interface SpringDataJpaLedgerTransactionRepository: JpaRepository<JpaLedgerTransactionEntity, Long> {

    fun existsByOrderId(orderId: String): Boolean
}