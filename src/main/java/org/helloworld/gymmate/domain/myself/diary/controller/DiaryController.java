package org.helloworld.gymmate.domain.myself.diary.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryCreateRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryResponse;
import org.helloworld.gymmate.domain.myself.diary.service.DiaryService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
	private final DiaryService diaryService;

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PostMapping
	public ResponseEntity<Long> createDiary(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@Valid @RequestBody DiaryCreateRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(
			diaryService.createDiary(request, customOAuth2User.getUserId()));
	}

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@DeleteMapping(value = "/{diaryId}")
	public ResponseEntity<Void> deleteDiary(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long diaryId) {

		diaryService.deleteDiary(diaryId, customOAuth2User.getUserId());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PutMapping(value = "/{diaryId}")
	public ResponseEntity<Long> modifyDiary(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long diaryId,
		@Valid @RequestBody DiaryRequest request) {

		return ResponseEntity.status(HttpStatus.OK).body(
			diaryService.modifyDiary(diaryId, request, customOAuth2User.getUserId()));
	}

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@GetMapping
	@Validated
	public ResponseEntity<PageDto<DiaryResponse>> getDiaries(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam @Min(0) int page,
		@RequestParam @Min(1) @Max(31) int size) {

		return ResponseEntity.ok(PageMapper.toPageDto(
			diaryService.getDiaries(page, size, customOAuth2User.getUserId())));
	}
}
