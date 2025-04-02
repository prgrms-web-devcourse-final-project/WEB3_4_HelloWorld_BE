package org.helloworld.gymmate.domain.gym.machine.mapper;

import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineCreateRequest;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;

import jakarta.validation.Valid;

public class MachineMapper {
	public static Machine toEntity(@Valid MachineCreateRequest request, String imageUrl, Gym gym) {
		return Machine.builder()
			.machineName(request.name())
			.amount(request.amount())
			.machineImage(imageUrl)
			.gym(gym)
			.build();
	}
}
