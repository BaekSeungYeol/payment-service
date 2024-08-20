package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.domain.DoubleLedgerEntry

interface LedgerEntryRepository {

    fun save(doubleLedgerEntries: List<DoubleLedgerEntry>)
}