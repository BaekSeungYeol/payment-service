package me.whitewin.ledgerservice.ledger.application.port.out.persistence.repository

import me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity.JpaPaymentOrderEntity
import me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity.JpaPaymentOrderMapper
import me.whitewin.ledgerservice.ledger.domain.PaymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPaymentOrderRepository(
    private val springDataJpaPaymentOrderRepository: SpringDataJpaPaymentOrderRepository,
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