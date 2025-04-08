package org.helloworld.gymmate.domain.pt.student.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.pt.student.dto.StudentsResponse;
import org.helloworld.gymmate.domain.pt.student.service.StudentService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;

	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@GetMapping
	@Validated
	public ResponseEntity<PageDto<StudentsResponse>> getStudents(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam(defaultValue = "0") @Min(0) int page,
		@RequestParam(defaultValue = "6") @Min(1) @Max(50) int pageSize
	) {
		return ResponseEntity.ok(PageMapper.toPageDto(
			studentService.getStudents(customOAuth2User.getUserId(), page, pageSize)));
	}

}
