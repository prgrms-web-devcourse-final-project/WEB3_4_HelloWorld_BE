package org.helloworld.gymmate.domain.pt.classtime.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.helloworld.gymmate.domain.pt.classtime.service.ClassTimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/classtime")
@RequiredArgsConstructor
public class ClassTimeController {
	private final ClassTimeService classTimeService;

	@PostMapping
	public ResponseEntity<Map<String,Long>> createClassTime(
		@RequestBody @Valid ClassTimeRequest request
	){
		// userDetail 넘겨줘야 함
		ClassTime classTime = classTimeService.createClassTime(request);
		return ResponseEntity.ok(
			Map.of("classTimeId",classTime.getClassTimeId()));
	}

}
