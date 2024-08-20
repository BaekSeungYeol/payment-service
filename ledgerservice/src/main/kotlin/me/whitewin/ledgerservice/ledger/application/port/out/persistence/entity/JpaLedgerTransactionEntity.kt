package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "ledger_transactions")
class JpaLedgerTransactionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val description: String,

    @Column(name = "reference_id", nullable = false)
    val referenceId: Long,

    @Size(max = 50)
    @Column(name = "reference_type", length = 50)
    val referenceType: String,

    @Column(name = "order_id")
    val orderId: String,

    @Column(name = "idempotency_key", nullable = false)
    val idempotencyKey: String,
)