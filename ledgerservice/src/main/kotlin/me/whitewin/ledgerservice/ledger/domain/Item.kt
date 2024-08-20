package me.whitewin.ledgerservice.ledger.domain

open class Item(
    val id: Long,
    val amount: Long,
    val orderId: String,
    val type: ReferenceType
)