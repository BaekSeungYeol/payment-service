package me.whitewin.ledgerservice.ledger.application.port.`in`

import me.whitewin.ledgerservice.ledger.domain.LedgerEventMessage
import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage

interface DoubleLedgerEntryRecordUseCase {

    fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage
}