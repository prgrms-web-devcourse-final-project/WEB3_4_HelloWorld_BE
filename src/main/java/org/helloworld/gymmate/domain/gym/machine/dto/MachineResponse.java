package org.helloworld.gymmate.domain.gym.machine.dto;

public record MachineResponse(
	Long machineId,
	String machineName,
	Integer amount,
	String machineImage
) {
}