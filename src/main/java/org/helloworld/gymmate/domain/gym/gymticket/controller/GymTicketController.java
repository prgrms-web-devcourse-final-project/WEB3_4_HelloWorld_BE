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

@Tag(name = "제휴 헬스장 이용권 API", description = "제휴 헬스장 이용권 구매 및 이용권 티켓 조회")
@RestController
@RequestMapping("/gymTicket")
@RequiredArgsConstructor
public class GymTicketController {
    private final GymTicketService gymTicketService;

    @Operation(summary = "[일반 회원] 제휴 헬스장 이용권 구매 가능 여부 확인", description = "일반 회원이 자신이 보유한 캐시로 선택한 제휴 헬스장 이용권 구매 가능 여부 조회")
    @GetMapping("/purchase/{gymProductId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<GymTicketPurchaseResponse> checkPurchaseAvailability(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymProductId
    ) {
        GymTicketPurchaseResponse response = gymTicketService.getPurchaseData(customOAuth2User.getUserId(),
            gymProductId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "[일반 회원]  제휴 헬스장 이용권 구매 및 티켓 생성", description = "일반 회원이 자신이 보유한 캐시로 선택한 제휴 헬스장 이용권 구매 및 이용권 티켓 생성")
    @PostMapping("/purchase/{gymProductId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<Long> buyGymProduct(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymProductId
    ) {
        Long ticketId = gymTicketService.createTicket(customOAuth2User.getUserId(), gymProductId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketId);
    }

    @Operation(summary = "[일반 회원]  구매한 제휴 헬스장 이용권 티켓 목록", description = "일반 회원이 자신이 구매 및 보유하고 있는 이용권 티켓 목록 조회")
    @GetMapping("/member")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Validated
    public ResponseEntity<PageDto<MemberGymTicketResponse>> getMemberTickets(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(10) int pageSize
    ) {
        PageDto<MemberGymTicketResponse> pageResponse = PageMapper.toPageDto(
            gymTicketService.getMemberTickets(customOAuth2User.getUserId(), page, pageSize));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "[헬스장 운영자]  판매한 제휴 헬스장 이용권 티켓 목록", description = "헬스장 운영자가 자신이 운영중인 헬스장에서 판매한 헬스장 이용권 티켓 목록 조회")
    @GetMapping("/owner")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Validated
    public ResponseEntity<PageDto<PartnerGymTicketResponse>> getPartnerGymTickets(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(10) int pageSize
    ) {
        PageDto<PartnerGymTicketResponse> pageResponse = PageMapper.toPageDto(
            gymTicketService.getPartnerGymTickets(customOAuth2User.getUserId(), page, pageSize));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "[일반 회원]  제휴 헬스장 이용권 티켓 환불", description = "일반 회원이 자신이 구매한 이용권 티켓의 남은 기간 만큼 캐시 환불, 이후 티켓 목록에서 환불 상태로 표시")
    @PatchMapping("/{gymTicketId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<Void> cancelGymTicket(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long gymTicketId
    ) {
        gymTicketService.cancelTicket(customOAuth2User.getUserId(), gymTicketId);
        return ResponseEntity.ok().build();
    }
}
