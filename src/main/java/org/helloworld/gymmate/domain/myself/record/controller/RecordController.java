package org.helloworld.gymmate.domain.myself.record.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordModifyRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.myself.record.service.RecordService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody RecordCreateRequest request) {


        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        long recordId = recordService.createRecord(request, member);

        return ResponseEntity.ok(
                Map.of("recordId", recordId));
    }

    @DeleteMapping(value = "/{recordId}")
    public ResponseEntity<Map<String, Long>> deleteRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long recordId) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        recordService.deleteRecord(recordId, member);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{recordId}")
    public ResponseEntity<Map<String, Long>> modifyRecord(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long recordId,
            @Valid @RequestBody RecordModifyRequest request) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        recordService.modifyRecord(recordId, request, member);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageDto<RecordResponse>> getRecords(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam int page,
            @RequestParam int size) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());

        return ResponseEntity.ok(PageMapper.toPageDto(
                recordService.getRecords(page, size, member)));
    }
}
