package org.helloworld.gymmate.domain.payment.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTossOrderId(String tossOrderId);
}
