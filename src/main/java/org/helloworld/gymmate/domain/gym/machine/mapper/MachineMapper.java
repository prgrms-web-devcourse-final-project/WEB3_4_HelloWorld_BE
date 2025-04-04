package org.helloworld.gymmate.domain.gym.machine.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineRequest;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;

import jakarta.validation.Valid;

public class MachineMapper {
	public static Machine toEntity(@Valid MachineRequest request, String imageUrl, Gym gym) {
		return Machine.builder()
			.machineName(request.machineName())
			.amount(request.amount())
			.machineImage(imageUrl)
			.gym(gym)
			.build();
	}

	public static MachineResponse toDto(Machine machine) {
		return new MachineResponse(
			machine.getMachineId(),
			machine.getMachineName(),
			machine.getAmount(),
			machine.getMachineImage()
		);
	}

	public static List<MachineResponse> toDtoList(List<Machine> machines) {
		return machines.stream()
			.map(MachineMapper::toDto)
			.collect(Collectors.toList());
	}
}
