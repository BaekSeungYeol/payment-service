package me.whitewin.ledgerservice.ledger.application.port.out.persistence

import me.whitewin.ledgerservice.ledger.domain.DoubleAccountsForLedger
import me.whitewin.ledgerservice.ledger.domain.FinanceType


interface LoadAccountPort {
    fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}