package org.helloworld.gymmate.domain.gym.machine.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineRequest;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;

public class MachineMapper {
	public static Machine toEntity(MachineRequest request, String imageUrl, Gym gym) {
		return toEntity(request.machineName(), request.amount(), imageUrl, gym);
	}

	public static Machine toEntity(String machineName, Integer amount, String imageUrl, Gym gym) {
		return Machine.builder()
			.machineName(machineName)
			.amount(amount)
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
