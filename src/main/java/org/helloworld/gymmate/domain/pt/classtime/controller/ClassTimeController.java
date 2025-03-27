package org.helloworld.gymmate.domain.pt.classtime.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.helloworld.gymmate.domain.pt.classtime.service.ClassTimeService;
import org.springframework.http.ResponseEntity;
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
public class ClassTimeController {
	private final ClassTimeService classTimeService;

	@PostMapping
	public ResponseEntity<Map<String,Long>> createClassTime(
		@RequestBody @Valid ClassTimeRequest request
	){
		// TODO : userDetail 넘겨줘야 함
		ClassTime classTime = classTimeService.createClassTime(request);
		return ResponseEntity.ok(
			Map.of("classTimeId",classTime.getClassTimeId()));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteClassTime(
		@RequestParam("dayOfWeek") Integer dayOfWeek,
		@RequestParam("time") Integer time
	){
		// TODO : userDetail 넘겨줘야 함
		classTimeService.deleteClassTime(dayOfWeek,time);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<ClassTimesResponse> getClassTimes(){
		// TODO : userDetail 넘겨줘야 함
		return ResponseEntity.ok(
			classTimeService.getAvailableTimes()
		);
	}

}
