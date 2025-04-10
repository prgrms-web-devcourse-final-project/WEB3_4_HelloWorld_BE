package org.helloworld.gymmate.domain.payment.mapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.helloworld.gymmate.domain.payment.dto.response.PaymentResponse;
import org.helloworld.gymmate.domain.payment.entity.Payment;

public class PaymentMapper {

    public static Payment toEntity(PaymentResponse response) {
        DateTimeFormatter isoOffsetDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(response.approvedAt(), isoOffsetDateTime);

        return Payment.builder()
            .tossOrderId(response.orderId())
            .paymentKey(response.paymentKey())
            .paidAmount(response.totalAmount())
            .successDate(offsetDateTime.toLocalDateTime())
            .method(response.method())
            .build();
    }
}
