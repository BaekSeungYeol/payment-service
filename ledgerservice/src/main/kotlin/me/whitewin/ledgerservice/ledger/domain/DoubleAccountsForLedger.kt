package me.whitewin.ledgerservice.ledger.domain

data class DoubleAccountsForLedger(
    val to: Account,
    val from: Account
)