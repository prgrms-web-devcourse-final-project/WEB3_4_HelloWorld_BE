package org.helloworld.gymmate.domain.pt.classtime.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.helloworld.gymmate.domain.pt.classtime.service.ClasstimeService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "수업 가능시간 API", description = "트레이너의 수업 가능시간 등록, 삭제, 조회")
@RestController
@RequestMapping("/classtime")
@RequiredArgsConstructor
public class ClasstimeController {
    private final ClasstimeService classtimeService;

    @Operation(summary = "트레이너 - 수업 가능시간 생성")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Map<String, Long>> createClasstime(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid ClasstimeRequest request
    ) {
        Classtime classTime = classtimeService.createClasstime(request, customOAuth2User.getUserId());
        return ResponseEntity.ok(
            Map.of("classTimeId", classTime.getClasstimeId()));
    }

    @Operation(summary = "트레이너 - 수업 가능시간 삭제")
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deleteClasstime(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam("dayOfWeek") Integer dayOfWeek,
        @RequestParam("time") Integer time
    ) {
        classtimeService.deleteClasstime(dayOfWeek, time, customOAuth2User.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 - 본인 수업 가능시간 전체조회")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<ClasstimesResponse> getClasstimes(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseEntity.ok(
            classtimeService.getAvailableTimes(customOAuth2User.getUserId()));
    }

    @Operation(summary = "멤버 - 요일별 수업 가능시간(겸 예약 가능시간) 전체조회")
    @GetMapping("/{trainerId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<ClasstimesResponse> getTrainersClassTimes(
        @PathVariable long trainerId
    ) {
        return ResponseEntity.ok(
            classtimeService.getAvailableTimes(trainerId));
    }

}
