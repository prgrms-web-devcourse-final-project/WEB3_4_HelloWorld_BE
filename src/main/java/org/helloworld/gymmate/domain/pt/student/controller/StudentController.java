package org.helloworld.gymmate.domain.pt.student.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.pt.student.dto.StudentDetailResponse;
import org.helloworld.gymmate.domain.pt.student.dto.StudentsResponse;
import org.helloworld.gymmate.domain.pt.student.service.StudentService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "수강생 API", description = "트레이너의 수강생 조회")
@RestController
@RequestMapping("/trainer/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "수강생 전체조회")
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

    @Operation(summary = "수강생 단일조회")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping("/{studentId}")
    @Validated
    public ResponseEntity<StudentDetailResponse> getStudent(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(studentService.getStudent(customOAuth2User.getUserId(), studentId));
    }
}
