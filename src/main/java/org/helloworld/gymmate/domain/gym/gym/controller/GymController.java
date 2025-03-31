package org.helloworld.gymmate.domain.gym.gym.controller;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.service.GymService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {

	private final GymService gymService;
	private final ObjectMapper objectMapper;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GymResponse> createGym(
		@RequestPart("request") String requestJson,
		@RequestPart("images") List<MultipartFile> images)throws JsonProcessingException {

		// JSON 문자열을 record로 변환
		GymCreateRequest request = objectMapper.readValue(requestJson, GymCreateRequest.class);

		GymResponse response = gymService.createGym(request, images);
		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "/{gymId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GymResponse> updateGym(
		@PathVariable Long gymId,
		@RequestPart("request") String requestJson,
		@RequestPart(value = "images" , required = false) List<MultipartFile> images)throws JsonProcessingException {

		GymUpdateRequest request = objectMapper.readValue(requestJson, GymUpdateRequest.class);
		GymResponse response = gymService.updateGym(gymId, request, images);
		return ResponseEntity.ok(response);
	}
}
