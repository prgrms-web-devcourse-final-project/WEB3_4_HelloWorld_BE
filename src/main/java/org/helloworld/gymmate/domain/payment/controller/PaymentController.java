package org.helloworld.gymmate.domain.payment.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.domain.payment.dto.request.PaymentRequest;
import org.helloworld.gymmate.domain.payment.dto.response.PaymentResponse;
import org.helloworld.gymmate.domain.payment.service.PaymentService;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final MemberService memberService;

    /**
     * 프론트엔드에서 Toss 결제 UI 결제 요청 완료 후 redirect된 파라미터(paymentKey, orderId, amount)를
     * 포함한 PaymentRequest를 전송하면 해당 요청에 대해 결제 승인 처리를 진행하고,
     * 승인 결과에 따라 성공 페이지 또는 실패 페이지로 리다이렉션합니다.
     */
    @PostMapping("/confirm")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> confirmPayment(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.confirm(
                memberService.findByUserId(customOAuth2User.getUserId()),
                request);

            // 결제 승인 성공 시 front-end 성공 페이지로 redirect
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/order/success" +
                "?orderId=" + response.orderId() +
                "&amount=" + response.totalAmount() +
                "&paymentKey=" + response.paymentKey()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (BusinessException e) {
            log.error("결제 승인 실패: {}", e.getMessage(), e);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/order/fail" +
                "?code=PAYMENT_ERROR" +
                "&message=" + encodeParam(e.getMessage())));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("서버 오류 발생: {}", e.getMessage(), e);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/order/fail" +
                "?code=SERVER_ERROR" +
                "&message=" + encodeParam("결제 처리 중 오류가 발생했습니다.")));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }

    private String encodeParam(String param) {
        try {
            return java.net.URLEncoder.encode(param, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return param;
        }
    }
}
