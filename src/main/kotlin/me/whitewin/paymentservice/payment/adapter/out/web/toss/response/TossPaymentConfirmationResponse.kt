package me.whitewin.paymentservice.payment.adapter.out.web.toss.response

data class TossPaymentConfirmationResponse(
    val version: String,
    val paymentKey: String,
    val type: String,
    val orderId: String,
    val orderName: String,
    val mId: String,
    val currency: String,
    val method: String,
    val totalAmount: Long,
    val balanceAmount: Long,
    val status: String,
    val requestedAt: String,
    val approvedAt: String,
    val useEscrow: Boolean,
    val lastTransactionKey: String?,
    val transactionKey: String?,
    val suppliedAmount: Long,
    val vat: Long,
    val cultureExpense: Boolean,
    val taxFreeAmount: Long,
    val taxExemptionAmount: Long,
    val cancels: List<Cancel>?,
    val isPartialCancelable: Boolean,
    val card: Card?,
    val virtualAccount: VirtualAccount?
)

data class Cancel(
    val cancelAmount: Long,
    val cancelReason: String,
    val taxFreeAmount: Long,
    val taxExemptionAmount: Long,
    val refundableAmount: Long,
    val easyPayDiscountAmount: Long,
    val canceledAt: String,
    val transactionKey: String,
    val receiptKey: String?,
    val cancelStatus: String,
    val cancelRequestId: String?
)

data class Card(
    val amount: Long,
    val issuerCode: String,
    val acquirerCode: String?,
    val number: String,
    val installmentPlanMonths: Int,
    val approveNo: String,
    val useCardPoint: Boolean,
    val cardType: String,
    val ownerType: String,
    val acquireStatus: String,
    val isInterestFree: Boolean,
    val interestPayer: String?
)

data class VirtualAccount(
    val accountType: String,
    val accountNumber: String,
    val bankCode: String,
    val customerName: String,
    val dueDate: String,
    val refundStatus: String,
    val expired: Boolean,
    val settlementStatus: String,
    val refundReceiveAccount: RefundReceiveAccount?
)

data class RefundReceiveAccount(
    val bank: String,
    val accountNumber: String,
    val holderName: String
)

data class MobilePhone(
    val customerMobilePhone: String,
    val settlementStatus: String
)

data class GiftCertificate(
    val approveNo: String,
    val settlementStatus: String
)

data class Transfer(
    val bankCode: String,
    val settlementStatus: String
)

data class Receipt(
    val url: String
)

data class Checkout(
    val url: String
)

data class EasyPay(
    val amount: Long,
    val provider: String
)

data class Failure(
    val code: String,
    val message: String
)

data class CashReceipt(
    val type: String,
    val amount: Long,
    val taxFreeAmount: Long,
    val issuanceStatus: String,
    val receiptKey: String
)

data class Discount(
    val amount: Long
)

