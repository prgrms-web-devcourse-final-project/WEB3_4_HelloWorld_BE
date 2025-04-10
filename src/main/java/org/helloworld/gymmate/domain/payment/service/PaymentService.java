package org.helloworld.gymmate.domain.payment.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.payment.dto.request.PaymentRequest;
import org.helloworld.gymmate.domain.payment.dto.response.PaymentResponse;
import org.helloworld.gymmate.domain.payment.mapper.PaymentMapper;
import org.helloworld.gymmate.domain.payment.properties.PaymentProperties;
import org.helloworld.gymmate.domain.payment.repository.PaymentRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final PaymentProperties paymentProperties;

    @Transactional
    public PaymentResponse confirm(Member member, PaymentRequest paymentRequest) {
        PaymentResponse response;
        try {
            String encodedKey = Base64.getEncoder()
                .encodeToString((paymentProperties.secretKey() + ":").getBytes(StandardCharsets.UTF_8));
            response = restClient.post()
                .uri(paymentProperties.confirmUrl())
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.set("Authorization", "Basic " + encodedKey);
                })
                .body(paymentRequest)
                .retrieve()
                .body(PaymentResponse.class);
        } catch (RestClientException e) {
            throw new BusinessException(ErrorCode.PAYMENT_CONFIRM_FAILED);
        }
        // response가 null이거나 유효하지 않은 금액인 경우
        if (response == null || response.totalAmount() <= 0) {
            log.error("결제 응답이 유효하지 않습니다. response={}", response);
            throw new BusinessException(ErrorCode.PAYMENT_CONFIRM_FAILED);
        }
        log.debug("결제 성공 : {}, {}", response.orderId(), response.totalAmount());
        log.debug("결제 응답 : {}", response);
        member.updateCash(member.getCash() + response.totalAmount()); // 결제 성공시 회원 캐시 업데이트
        paymentRepository.save(PaymentMapper.toEntity(response));
        return response;
    }
}
