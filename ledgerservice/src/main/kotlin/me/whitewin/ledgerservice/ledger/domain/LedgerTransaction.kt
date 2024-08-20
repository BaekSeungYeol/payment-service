package me.whitewin.ledgerservice.ledger.domain

data class LedgerTransaction(
    val referenceType: ReferenceType,
    val referenceId: Long,
    val orderId: String
)