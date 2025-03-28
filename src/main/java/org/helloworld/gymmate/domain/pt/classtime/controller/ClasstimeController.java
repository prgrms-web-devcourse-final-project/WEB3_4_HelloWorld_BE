package org.helloworld.gymmate.domain.pt.classtime.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.helloworld.gymmate.domain.pt.classtime.service.ClasstimeService;
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
public class ClasstimeController {
	private final ClasstimeService classtimeService;

	@PostMapping
	public ResponseEntity<Map<String,Long>> createClasstime(
		@RequestBody @Valid ClasstimeRequest request
	){
		// TODO : userDetail 넘겨줘야 함
		Classtime classTime = classtimeService.createClasstime(request);
		return ResponseEntity.ok(
			Map.of("classTimeId",classTime.getClasstimeId()));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteClasstime(
		@RequestParam("dayOfWeek") Integer dayOfWeek,
		@RequestParam("time") Integer time
	){
		// TODO : userDetail 넘겨줘야 함
		classtimeService.deleteClasstime(dayOfWeek,time);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<ClasstimesResponse> getClasstimes(){
		// TODO : userDetail 넘겨줘야 함
		return ResponseEntity.ok(
			classtimeService.getAvailableTimes()
		);
	}

}
