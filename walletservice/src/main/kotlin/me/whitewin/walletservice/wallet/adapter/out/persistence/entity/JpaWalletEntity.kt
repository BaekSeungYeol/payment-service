package me.whitewin.walletservice.wallet.adapter.out.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "wallets")
class JpaWalletEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotNull
    @Column(name = "user_id")
    val userId: Long,

    val balance: BigDecimal,

    @Version
    val version: Int,
)