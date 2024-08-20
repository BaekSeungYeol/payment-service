package me.whitewin.ledgerservice.ledger.application.port.out

import me.whitewin.ledgerservice.ledger.domain.DoubleLedgerEntry

interface SaveDoubleLedgerEntryPort {

    fun save(doubleLedgerEntries: List<DoubleLedgerEntry>)
}