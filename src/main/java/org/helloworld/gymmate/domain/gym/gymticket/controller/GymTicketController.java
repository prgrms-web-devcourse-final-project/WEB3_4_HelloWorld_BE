package org.helloworld.gymmate.domain.gym.gymticket.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.gym.gymticket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymticket.dto.MemberGymTicketResponse;
import org.helloworld.gymmate.domain.gym.gymticket.dto.PartnerGymTicketResponse;
import org.helloworld.gymmate.domain.gym.gymticket.service.GymTicketService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "제휴 헬스장 이용권 API", description = "제휴 헬스장 이용권의 구매 및 판매 API")
@RestController
@RequestMapping("/gymTicket")
@RequiredArgsConstructor
public class GymTicketController {
    private final GymTicketService gymTicketService;

    @Operation(summary = "[일반 회원] 제휴 헬스장 이용권 구매 가능 여부 확인", description = "제휴 헬스장 이용권 구매 페이지에서 구매 가능 여부 반환")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/purchase/{gymProductId}")
    public ResponseEntity<GymTicketPurchaseResponse> checkPurchaseAvailability(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymProductId
    ) {
        return ResponseEntity.ok().body(
            gymTicketService.getPurchaseData(customOAuth2User.getUserId(), gymProductId));
    }

    @Operation(summary = "[일반 회원]  제휴 헬스장 이용권 구매", description = "제휴 헬스장 이용권 구매 페이지에서 이용권 구매")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/purchase/{gymProductId}")
    public ResponseEntity<Long> buyGymProduct(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymProductId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            gymTicketService.createTicket(customOAuth2User.getUserId(), gymProductId));
    }

    @Operation(summary = "[일반 회원]  구매한 제휴 헬스장 이용권 목록", description = "일반 회원이 자신이 구매한 이용권 목록 반환")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member")
    @Validated
    public ResponseEntity<PageDto<MemberGymTicketResponse>> getMemberTickets(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(10) int pageSize
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(
            gymTicketService.getMemberTickets(customOAuth2User.getUserId(), page, pageSize)));
    }

    @Operation(summary = "[헬스장 운영자]  판매한 제휴 헬스장 이용권 목록 ", description = "자신이 운영중인 헬스장에서 판매한 헬스장 이용권 목록을 반환")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping("/owner")
    @Validated
    public ResponseEntity<PageDto<PartnerGymTicketResponse>> getPartnerGymTickets(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(10) int pageSize
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(
            gymTicketService.getPartnerGymTickets(customOAuth2User.getUserId(), page, pageSize)));
    }

    @Operation(summary = "[일반 회원]  제휴 헬스장 이용권 환불 ", description = "일반 회원이 자신이 구매한 이용권 환불")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PatchMapping("/{gymTicketId}")
    public ResponseEntity<Void> cancelGymTicket(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymTicketId
    ) {
        gymTicketService.cancelTicket(customOAuth2User.getUserId(), gymTicketId);
        return ResponseEntity.ok().build();
    }
}
