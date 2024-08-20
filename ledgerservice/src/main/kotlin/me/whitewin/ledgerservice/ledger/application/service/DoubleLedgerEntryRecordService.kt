package me.whitewin.ledgerservice.ledger.application.service

import me.whitewin.ledgerservice.common.UseCase
import me.whitewin.ledgerservice.ledger.application.port.`in`.DoubleLedgerEntryRecordUseCase
import me.whitewin.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import me.whitewin.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import me.whitewin.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.LoadAccountPort
import me.whitewin.ledgerservice.ledger.domain.*

@UseCase
class DoubleLedgerEntryRecordService(
    private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
    private val loadAccountPort: LoadAccountPort,
    private val loadPaymentOrderPort: LoadPaymentOrderPort,
    private val saveDoubleLedgerEntryPort: SaveDoubleLedgerEntryPort
): DoubleLedgerEntryRecordUseCase {

    override fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage {
        if(duplicateMessageFilterPort.isAlreadyProcess(message)) {
            return createLedgerEventMessage(message)
        }

        val doubleAccountsForLedger = loadAccountPort.getDoubleAccountsForLedger(FinanceType.PAYMENT_ORDER)
        val paymentOrders = loadPaymentOrderPort.getPaymentOrders(message.orderId())

        val doubleLedgerEntries = Ledger.createDoubleLedgerEntry(doubleAccountsForLedger, paymentOrders)
        saveDoubleLedgerEntryPort.save(doubleLedgerEntries)

        return createLedgerEventMessage(message)
    }

    private fun createLedgerEventMessage(message: PaymentEventMessage) =
        LedgerEventMessage(
            type = LedgerEventMessageType.SUCCESS,
            payload = mapOf(
                "orderId" to message.orderId(),
            )
        )
}