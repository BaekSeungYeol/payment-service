package me.whitewin.ledgerservice.ledger.application.service

import io.mockk.every
import io.mockk.mockk
import me.whitewin.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import me.whitewin.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import me.whitewin.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.LoadAccountPort
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.SpringDataJpaLedgerEntryRepository
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.SpringDataJpaLedgerTransactionRepository
import me.whitewin.ledgerservice.ledger.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class DoubleLedgerEntryRecordServiceTest(
    @Autowired private val springDataJpaLedgerEntryRepository: SpringDataJpaLedgerEntryRepository,
    @Autowired private val springDataJpaLedgerTransactionRepository: SpringDataJpaLedgerTransactionRepository,
    @Autowired private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
    @Autowired private val loadAccountPort: LoadAccountPort,
    @Autowired private val saveDoubleLedgerEntryPort: SaveDoubleLedgerEntryPort,
) {

    @Autowired
    private lateinit var doubleLedgerEntryRecordService: DoubleLedgerEntryRecordService

    @BeforeEach
    fun clean() {
        springDataJpaLedgerEntryRepository.deleteAll()
        springDataJpaLedgerTransactionRepository.deleteAll()
    }

    @Test
    fun `should record double ledger entries successfully`() {
        val paymentEventMessage = PaymentEventMessage(
            type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
            payload = mapOf(
                "orderId" to UUID.randomUUID().toString(),
            )
        )

        val mockLoadPaymentOrderPort = mockk<LoadPaymentOrderPort>()

        every { mockLoadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId())} returns listOf(
            PaymentOrder(
                id = 1L,
                amount = 200L,
                orderId = paymentEventMessage.orderId()
            ),
            PaymentOrder(
                id = 2L,
                amount = 300L,
                orderId = paymentEventMessage.orderId()
            )
        )

        val doubleLedgerEntryRecordService = DoubleLedgerEntryRecordService(
            duplicateMessageFilterPort = duplicateMessageFilterPort,
            loadAccountPort = loadAccountPort,
            loadPaymentOrderPort = mockLoadPaymentOrderPort,
            saveDoubleLedgerEntryPort = saveDoubleLedgerEntryPort
        )

        val ledgerEventMessage = doubleLedgerEntryRecordService.recordDoubleLedgerEntry(paymentEventMessage)

        val jpaLedgerEntryEntities = springDataJpaLedgerEntryRepository.findAll()

        val sumOf = jpaLedgerEntryEntities.sumOf {
            when(it.type) {
                LedgerEntryType.CREDIT -> it.amount
                LedgerEntryType.DEBIT -> it.amount.negate()
            }
        }
        assertThat(ledgerEventMessage.type).isEqualTo(LedgerEventMessageType.SUCCESS)
        assertThat(ledgerEventMessage.payload["orderId"]).isEqualTo(paymentEventMessage.orderId())
        assertThat(sumOf.toInt()).isEqualTo(0)
        assertThat(jpaLedgerEntryEntities.size).isEqualTo(4)
    }
}