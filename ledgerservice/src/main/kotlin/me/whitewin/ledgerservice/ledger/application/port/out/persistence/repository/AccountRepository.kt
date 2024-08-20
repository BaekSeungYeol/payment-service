package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.domain.DoubleAccountsForLedger
import me.whitewin.ledgerservice.ledger.domain.FinanceType

interface AccountRepository {

    fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}