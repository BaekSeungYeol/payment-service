package me.whitewin.walletservice.wallet.adapter.out.persistence.entity

import jakarta.persistence.*
import me.whitewin.walletservice.wallet.domain.PaymentStatus
import java.math.BigDecimal

@Entity
@Table(name = "payment_orders")
class JpaPaymentOrderEntity(
    @Id
    val id: Long? = null,
//    @Column(name = "payment_event_id")
//    val paymentEventId: Long? = null,
    @Column(name = "seller_id")
    val sellerId: Long,
//    @Column(name = "product_id")
//    val productId: Long,
    @Column(name = "order_id")
    val orderId: String,
    @Column(name = "amount")
    val amount: BigDecimal,
//    @Column(name = "payment_order_status")
//    @Enumerated(value = EnumType.STRING)
//    val paymentStatus: PaymentStatus,
//    @Column(name = "ledger_updated")
//    private var isLedgerUpdated: Boolean = false,
//    @Column(name = "wallet_updated")
//    private var isWalletUpdated: Boolean = false
)