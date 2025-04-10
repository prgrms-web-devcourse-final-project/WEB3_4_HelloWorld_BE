package org.helloworld.gymmate.domain.payment.dto.request;

import jakarta.validation.constraints.Positive;

public record PaymentRequest(
    String orderId,
    String paymentKey,
    @Positive Long amount
) {
}
