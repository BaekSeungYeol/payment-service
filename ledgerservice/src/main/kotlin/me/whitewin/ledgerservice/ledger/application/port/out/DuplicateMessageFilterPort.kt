package me.whitewin.ledgerservice.ledger.application.port.out

import me.whitewin.ledgerservice.ledger.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

    fun isAlreadyProcess(message: PaymentEventMessage): Boolean
}