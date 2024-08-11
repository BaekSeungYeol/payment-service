package me.whitewin.paymentservice.payment.adapter.out.stream

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Tag("ExternalIntegration")
class PaymentEventMessageSenderTest {

    @Test
    fun `should send eventMessage by using partitionKey`() {

    }
}