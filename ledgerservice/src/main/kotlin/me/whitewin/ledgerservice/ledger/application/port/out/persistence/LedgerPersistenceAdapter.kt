package me.whitewin.ledgerservice.ledger.application.port.out.persistence

import me.whitewin.ledgerservice.common.PersistentAdapter
import me.whitewin.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import me.whitewin.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.LedgerEntryRepository
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.LedgerTransactionRepository
import me.whitewin.ledgerservice.ledger.domain.DoubleLedgerEntry
import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage

@PersistentAdapter
class LedgerPersistenceAdapter(
    private val ledgerTransactionRepository: LedgerTransactionRepository,
    private val ledgerEntryRepository: LedgerEntryRepository,
): DuplicateMessageFilterPort, SaveDoubleLedgerEntryPort {

    override fun isAlreadyProcess(message: PaymentEventMessage): Boolean {
        return ledgerTransactionRepository.isExist(message)
    }

    override fun save(doubleLedgerEntries: List<DoubleLedgerEntry>) {
        return ledgerEntryRepository.save(doubleLedgerEntries)
    }
}