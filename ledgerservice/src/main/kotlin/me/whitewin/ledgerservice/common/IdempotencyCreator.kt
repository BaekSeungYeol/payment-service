package me.whitewin.ledgerservice.common

import java.util.UUID

object IdempotencyCreator {

    fun create(data: Any): String {
        return UUID.nameUUIDFromBytes(data.toString().toByteArray()).toString()
    }
}