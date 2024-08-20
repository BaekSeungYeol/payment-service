package me.whitewin.ledgerservice.ledger.application.port.out.persistence

import me.whitewin.ledgerservice.common.PersistentAdapter
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository.AccountRepository
import me.whitewin.ledgerservice.ledger.domain.DoubleAccountsForLedger
import me.whitewin.ledgerservice.ledger.domain.FinanceType

@PersistentAdapter
class AccountPersistenceAdapter(
    private val accountRepository: AccountRepository
): LoadAccountPort {

    override fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger {
        return accountRepository.getDoubleAccountsForLedger(financeType)
    }
}