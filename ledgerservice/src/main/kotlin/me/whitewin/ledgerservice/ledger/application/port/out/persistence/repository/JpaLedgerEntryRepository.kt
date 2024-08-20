package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity.JpaLedgeEntryEntity
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity.JpaLedgerEntryMapper
import me.whitewin.ledgerservice.ledger.domain.DoubleLedgerEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaLedgerEntryRepository(
    private val springDataJpaLedgerEntryRepository: SpringDataJpaLedgerEntryRepository,
    private val jpaLedgerEntryMapper: JpaLedgerEntryMapper
) : LedgerEntryRepository {
    override fun save(doubleLedgerEntries: List<DoubleLedgerEntry>) {
        springDataJpaLedgerEntryRepository.saveAll(doubleLedgerEntries.flatMap { jpaLedgerEntryMapper.mapToJpaEntity(it)})
    }
}

interface SpringDataJpaLedgerEntryRepository: JpaRepository<JpaLedgeEntryEntity, Long>