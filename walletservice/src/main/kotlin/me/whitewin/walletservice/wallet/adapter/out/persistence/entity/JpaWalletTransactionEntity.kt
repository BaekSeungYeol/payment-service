package me.whitewin.walletservice.wallet.adapter.out.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.whitewin.walletservice.wallet.domain.TransactionType
import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "wallet_transactions")
class JpaWalletTransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "wallet_id")
    val walletId: Long,

    @Column
    val amount: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    val type: TransactionType,

    @Column(name = "order_id")
    val orderId: String,

    @Column(name = "reference_type")
    val referenceType: String,

    @Column(name = "reference_id")
    val referenceId: Long,

    @Column(name = "idempotency_key")
    val idempotencyKey: String
)