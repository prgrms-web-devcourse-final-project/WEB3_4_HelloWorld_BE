package org.helloworld.gymmate.domain.pt.classtime.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.helloworld.gymmate.domain.pt.classtime.service.ClasstimeService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/classtime")
@RequiredArgsConstructor
public class ClasstimeController {
	private final ClasstimeService classtimeService;

	@PostMapping
	public ResponseEntity<Map<String, Long>> createClasstime(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid ClasstimeRequest request
	) {
		Classtime classTime = classtimeService.createClasstime(request, customOAuth2User.getUserId());
		return ResponseEntity.ok(
			Map.of("classTimeId", classTime.getClasstimeId()));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteClasstime(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam("dayOfWeek") Integer dayOfWeek,
		@RequestParam("time") Integer time
	) {
		classtimeService.deleteClasstime(dayOfWeek, time, customOAuth2User.getUserId());
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<ClasstimesResponse> getClasstimes(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	) {
		return ResponseEntity.ok(
			classtimeService.getAvailableTimes(customOAuth2User.getUserId())
		);
	}

}
