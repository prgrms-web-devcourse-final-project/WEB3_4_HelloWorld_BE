package org.helloworld.gymmate.domain.gym.gymInfo.controller;

import org.helloworld.gymmate.domain.gym.gymInfo.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "헬스장 API", description = "헬스장 지도 페이지")
@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {
	private final GymService gymService;

	@GetMapping("/{gymId}/facility")
	public ResponseEntity<FacilityAndMachineResponse> getFacilitiesAndMachines(
		@PathVariable Long gymId
	) {
		return ResponseEntity.ok(gymService.getOwnFacilitiesAndMachines(gymId));
	}
}
