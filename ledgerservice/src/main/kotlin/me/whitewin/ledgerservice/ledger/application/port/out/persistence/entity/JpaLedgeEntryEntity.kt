package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import me.whitewin.ledgerservice.ledger.domain.LedgerEntryType
import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "ledger_entries")
class JpaLedgeEntryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val amount: BigDecimal,

    @Column(name = "account_id")
    val accountId: Long,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val transaction: JpaLedgerTransactionEntity,

    @Enumerated(EnumType.STRING)
    val type: LedgerEntryType
)