package org.helloworld.gymmate.domain.myself.record.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.myself.record.service.RecordService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<Long> createRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody RecordCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                recordService.createRecord(request, customOAuth2User.getUserId()));
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping(value = "/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long recordId) {

        recordService.deleteRecord(recordId, customOAuth2User.getUserId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping(value = "/{recordId}")
    public ResponseEntity<Long> modifyRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long recordId,
            @Valid @RequestBody RecordRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(
                recordService.modifyRecord(recordId, request, customOAuth2User.getUserId()));
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping
    @Validated
    public ResponseEntity<PageDto<RecordResponse>> getRecords(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(31) int size) {

        return ResponseEntity.ok(PageMapper.toPageDto(
                recordService.getRecords(page, size, customOAuth2User.getUserId())));
    }
}
