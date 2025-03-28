package org.helloworld.gymmate.domain.gym.gym.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.service.GymService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {

	private final GymService gymService;

	@PostMapping
	public ResponseEntity<GymResponse> createGym
		(@RequestBody @Valid GymCreateRequest request){
			GymResponse response = gymService.createGym(request);
			return ResponseEntity.ok(response);
	}
}
