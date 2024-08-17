package me.whitewin.walletservice.wallet.adapter.out.persistence.repository

import me.whitewin.walletservice.wallet.adapter.out.persistence.entity.JpaWalletEntity
import me.whitewin.walletservice.wallet.adapter.out.persistence.entity.JpaWalletMapper
import me.whitewin.walletservice.wallet.adapter.out.persistence.exception.RetryExhaustedWithOptimisticLockingFailureException
import me.whitewin.walletservice.wallet.domain.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate
import java.math.BigDecimal

@Repository
class JpaWalletRepository(
    private val springDataJpaWalletRepository: SpringDataJpaWalletRepository,
    private val jpaWalletMapper: JpaWalletMapper,
    private val walletTransactionRepository: WalletTransactionRepository,
    private val transactionTemplate: TransactionTemplate
): WalletRepository {

    override fun getWallets(sellerIds: Set<Long>): Set<Wallet> {
        return springDataJpaWalletRepository.findByUserIdIn(sellerIds)
            .map { jpaWalletMapper.mapToDomainEntity(it) }
            .toSet()
    }

    override fun save(wallets: List<Wallet>) {
        try {
            performSaveOperation(wallets)
        } catch (e: ObjectOptimisticLockingFailureException) {
            retrySaveOperation(wallets)
        }
    }

    private fun retrySaveOperation(wallets: List<Wallet>, maxRetries: Int = 3, baseDelay: Int = 100) {
        var retryCount = 0

        while(true) {
            try {
                performSaveOperationWIthRecent(wallets)
                break
            } catch (e: ObjectOptimisticLockingFailureException) {
                if(++retryCount > maxRetries) {
                    throw RetryExhaustedWithOptimisticLockingFailureException(e.message ?: "exhausted retry count.")
                }
                waitForNextRetry(baseDelay)
            }
        }
    }

    private fun waitForNextRetry(baseDelay: Int) {
        val jitter = (Math.random() * baseDelay).toLong()

        try {
            Thread.sleep(jitter)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("Interrupted during retry wait", e)
        }
    }

    private fun performSaveOperationWIthRecent(wallets: List<Wallet>) {
        val recentWallets = springDataJpaWalletRepository.findByIdIn(wallets.map { it.id }.toSet())
        val recentWalletsById = recentWallets.associateBy { it.id }

        val walletPairs = wallets.map { wallet ->
            Pair(wallet, recentWalletsById[wallet.id]!!)
        }

        val updatedWallet = walletPairs.map {
            it.second.addBalance(BigDecimal(it.first.walletTransactions.sumOf { walletTransaction -> walletTransaction.amount}))
        }

        transactionTemplate.execute {
            springDataJpaWalletRepository.saveAll(updatedWallet)
            walletTransactionRepository.save(wallets.flatMap { it.walletTransactions })
        }


    }

    private fun performSaveOperation(wallets: List<Wallet>) {
        transactionTemplate.execute {
            springDataJpaWalletRepository.saveAll(wallets.map { jpaWalletMapper.mapToJpaEntity(it) })
            walletTransactionRepository.save(wallets.flatMap { it.walletTransactions })
        }
    }
}

interface SpringDataJpaWalletRepository : JpaRepository<JpaWalletEntity, Long> {

    fun findByUserIdIn(userIds: Set<Long>): List<JpaWalletEntity>
    fun findByIdIn(ids: Set<Long>) : List<JpaWalletEntity>
}