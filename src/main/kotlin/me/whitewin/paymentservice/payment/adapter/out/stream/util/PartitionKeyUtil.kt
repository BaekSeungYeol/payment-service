package me.whitewin.paymentservice.payment.adapter.out.stream.util

import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class PartitionKeyUtil {

    fun createPartitionKey(number: Int): Int {
        return abs(number) % Companion.PARTITION_KEY_COUNT
    }

    companion object {
        private const val PARTITION_KEY_COUNT = 6
    }
}