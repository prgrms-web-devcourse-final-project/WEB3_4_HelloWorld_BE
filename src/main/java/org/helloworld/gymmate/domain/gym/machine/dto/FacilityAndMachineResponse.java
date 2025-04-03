package org.helloworld.gymmate.domain.gym.machine.dto;

import java.util.List;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityResponse;

public record FacilityAndMachineResponse(
	FacilityResponse facilityResponse,
	List<MachineResponse> machineResponses
) {
}
