package org.helloworld.gymmate.domain.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "toss_order_id", unique = true)
    private String tossOrderId; // 토스 페이먼츠 주문 ID

    @Column(name = "payment_key")
    private String paymentKey; // 토스 페이먼츠 결제 키

    @Column(name = "paid_amount")
    private Long paidAmount; // 결제 금액

    @Column(name = "success_date")
    private LocalDateTime successDate; // 결제 완료일

    private String method; // 결제 수단
}
