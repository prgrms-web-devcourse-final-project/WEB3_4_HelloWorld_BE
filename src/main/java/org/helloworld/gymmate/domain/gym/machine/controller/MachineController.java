package org.helloworld.gymmate.domain.gym.machine.controller;

import org.helloworld.gymmate.common.validate.custom.ValidSingleImageFile;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineCreateRequest;
import org.helloworld.gymmate.domain.gym.machine.service.MachineService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/machine")
@RequiredArgsConstructor
public class MachineController {

	private final MachineService machineService;

	@PostMapping
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	public ResponseEntity<Long> createMachine(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestPart("machineData") @Valid MachineCreateRequest request,
		@RequestPart(required = false) @ValidSingleImageFile MultipartFile image
	) {
		return ResponseEntity.ok().body(
			machineService.createMachine(customOAuth2User.getUserId(), request, image));
	}
}
