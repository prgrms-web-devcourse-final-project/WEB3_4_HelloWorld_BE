package org.helloworld.gymmate.domain.payment.dto.request;

public record PaymentRequest(
    String paymentKey,
    String orderId,
    Long amount
) {
}
