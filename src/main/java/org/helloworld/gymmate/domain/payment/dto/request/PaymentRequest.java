package org.helloworld.gymmate.domain.payment.dto.request;

public record PaymentRequest(
    String orderId,
    String paymentKey,
    Long amount
) {
}
