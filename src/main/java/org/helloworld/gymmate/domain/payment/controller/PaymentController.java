package org.helloworld.gymmate.domain.payment.controller;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.domain.payment.dto.request.PaymentRequest;
import org.helloworld.gymmate.domain.payment.dto.response.PaymentResponse;
import org.helloworld.gymmate.domain.payment.service.PaymentService;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
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

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> confirmPayment(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.confirm(
                memberService.findByUserId(customOAuth2User.getUserId()),
                request);
            return ResponseEntity.status(HttpStatus.OK)
                .body(response);
        } catch (BusinessException e) {
            log.error("결제 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getErrorCode());
        }
    }
}
