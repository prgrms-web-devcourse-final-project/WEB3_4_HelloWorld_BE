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
import org.helloworld.gymmate.security.model.GymmateUserDetails;
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
            @AuthenticationPrincipal GymmateUserDetails userDetails, //TODO: Oauth2User로 변경
            @Valid @RequestBody RecordCreateRequest request) {

        long recordId = 0;
        Member member = null; //TODO: Oauth2User.getMember
        recordService.createRecord(request, member);

        return ResponseEntity.ok(
                Map.of("recordId", recordId));
    }

    @DeleteMapping(value = "/{recordId}")
    public ResponseEntity<Map<String, Long>> deleteRecord(
            @AuthenticationPrincipal GymmateUserDetails userDetails, //TODO: Oauth2User로 변경
            @PathVariable Long recordId) {

        Member member = null; //TODO: Oauth2User.getMember
        recordService.deleteRecord(recordId, member);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{recordId}")
    public ResponseEntity<Map<String, Long>> modifyRecord(
            @AuthenticationPrincipal GymmateUserDetails userDetails, //TODO: Oauth2User로 변경
            @PathVariable Long recordId,
            @Valid @RequestBody RecordModifyRequest request) {

        Member member = null; //TODO: Oauth2User.getMember
        recordService.modifyRecord(recordId, request, member);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageDto<RecordResponse>> getRecords(
            @AuthenticationPrincipal GymmateUserDetails userDetails, //TODO: Oauth2User로 변경
            @RequestParam int page,
            @RequestParam int size) {

        Member member = null; //TODO: Oauth2User.getMember

        return ResponseEntity.ok(PageMapper.toPageDto(
                recordService.getRecords(page, size, member)));
    }
}
