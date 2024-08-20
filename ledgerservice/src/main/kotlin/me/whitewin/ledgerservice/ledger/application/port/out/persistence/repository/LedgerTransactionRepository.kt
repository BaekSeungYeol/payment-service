package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage

interface LedgerTransactionRepository {
    fun isExist(message: PaymentEventMessage): Boolean
}