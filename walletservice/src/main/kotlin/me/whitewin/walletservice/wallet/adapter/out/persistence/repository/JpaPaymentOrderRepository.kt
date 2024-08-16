package me.whitewin.walletservice.wallet.adapter.out.persistence.repository

import me.whitewin.walletservice.wallet.adapter.out.persistence.entity.JpaPaymentOrderEntity
import me.whitewin.walletservice.wallet.adapter.out.persistence.entity.JpaPaymentOrderMapper
import me.whitewin.walletservice.wallet.domain.PaymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPaymentOrderRepository(
    private val springDataJpaPaymentOrderRepository: SpringDataJpaPaymentOrderRepository,
    private val japPaymentOrderMapper: JpaPaymentOrderMapper,
    private val jpaPaymentOrderMapper: JpaPaymentOrderMapper
): PaymentOrderRepository {

    override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
        return springDataJpaPaymentOrderRepository.findByOrderId(orderId)
            .map { jpaPaymentOrderMapper.mapToDomainEntity(it) }
    }
}

interface SpringDataJpaPaymentOrderRepository: JpaRepository<JpaPaymentOrderEntity, Long> {

    fun findByOrderId(orderId: String): List<JpaPaymentOrderEntity>
}