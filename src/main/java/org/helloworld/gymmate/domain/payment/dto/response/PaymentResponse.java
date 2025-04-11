package org.helloworld.gymmate.domain.payment.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponse(
    @JsonProperty("mId")
    String mid,
    String version,
    String paymentKey,
    String orderId,
    String orderName,
    String currency,
    String method,
    String status,
    String requestedAt,
    String approvedAt,
    Boolean useEscrow,
    Boolean cultureExpense,
    String type,
    String country,
    String lastTransactionKey,
    Boolean isPartialCancelable,
    @JsonProperty("totalAmount")
    Long totalAmount,
    @JsonProperty("balanceAmount")
    Long balanceAmount,
    @JsonProperty("suppliedAmount")
    Long suppliedAmount,
    @JsonProperty("vat")
    Long vat,
    @JsonProperty("taxFreeAmount")
    Long taxFreeAmount,
    @JsonProperty("taxExemptionAmount")
    Long taxExemptionAmount,
    // 결제 취소 이력
    List<CancelHistory> cancels,
    // 카드 결제 정보
    CardPaymentInfo card,
    // 가상계좌 결제 정보
    VirtualAccountInfo virtualAccount,
    // 휴대폰 결제 정보
    MobilePhoneInfo mobilePhone,
    // 상품권 결제 정보
    GiftCertificateInfo giftCertificate,
    // 계좌이체 정보
    TransferInfo transfer,
    // 간편결제 정보
    EasyPayInfo easyPay,
    // 현금영수증 정보
    CashReceiptInfo cashReceipt,
    // 현금영수증 발행 및 취소 이력
    List<CashReceiptHistory> cashReceipts,
    // 영수증 정보
    ReceiptInfo receipt,
    // 결제창 정보
    CheckoutInfo checkout,
    // 결제 실패 정보
    FailureInfo failure,
    // 메타데이터
    Map<String, String> metadata,
    // 할인 정보
    DiscountInfo discount,
    // 비밀 값 (웹훅 검증용)
    String secret
) {
    public record CancelHistory(
        Long cancelAmount,
        String cancelReason,
        Long taxFreeAmount,
        Long taxExemptionAmount,
        Long refundableAmount,
        Long transferDiscountAmount,
        Long easyPayDiscountAmount,
        OffsetDateTime canceledAt,
        String transactionKey,
        String receiptKey,
        String cancelStatus,
        String cancelRequestId
    ) {
    }

    public record CardPaymentInfo(
        Long amount,
        String issuerCode,
        String acquirerCode,
        String number,
        Integer installmentPlanMonths,
        String approveNo,
        Boolean useCardPoint,
        String cardType,
        String ownerType,
        String acquireStatus,
        Boolean isInterestFree,
        String interestPayer,
        String receiptUrl
    ) {
    }

    public record VirtualAccountInfo(
        String accountType,
        String accountNumber,
        String bankCode,
        String customerName,
        OffsetDateTime dueDate,
        String refundStatus,
        Boolean expired,
        String settlementStatus,
        RefundReceiveAccountInfo refundReceiveAccount
    ) {
    }

    public record RefundReceiveAccountInfo(
        String bankCode,
        String accountNumber,
        String holderName
    ) {
    }

    public record MobilePhoneInfo(
        String customerMobilePhone,
        String settlementStatus,
        String receiptUrl
    ) {
    }

    public record GiftCertificateInfo(
        String approveNo,
        String settlementStatus
    ) {
    }

    public record TransferInfo(
        String bankCode,
        String settlementStatus
    ) {
    }

    public record EasyPayInfo(
        String provider,
        Long amount,
        Long discountAmount
    ) {
    }

    public record CashReceiptInfo(
        String type,
        String receiptKey,
        String issueNumber,
        String receiptUrl,
        Long amount,
        Long taxFreeAmount
    ) {
    }

    public record CashReceiptHistory(
        String receiptKey,
        String orderId,
        String orderName,
        String type,
        String issueNumber,
        String receiptUrl,
        String businessNumber,
        String transactionType,
        Long amount,
        Long taxFreeAmount,
        String issueStatus,
        FailureInfo failure,
        String customerIdentityNumber,
        OffsetDateTime requestedAt
    ) {
    }

    public record ReceiptInfo(
        String url
    ) {
    }

    public record CheckoutInfo(
        String url
    ) {
    }

    public record FailureInfo(
        String code,
        String message
    ) {
    }

    public record DiscountInfo(
        Long amount
    ) {
    }
}
