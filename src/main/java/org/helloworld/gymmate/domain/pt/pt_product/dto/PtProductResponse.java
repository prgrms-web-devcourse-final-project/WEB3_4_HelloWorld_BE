package org.helloworld.gymmate.domain.pt.pt_product.dto;

import java.util.List;

public record PtProductResponse(
	Long ptProductId,
	Trainer trainer,
	String ptInfo,
	Long fee,
	List<String> images,
	Gym gym
) {
	public record Trainer(
		Long trainerId,
		String trainerName,
		String gender,
		String profile,
		String contact,
		String email,
		Double trainerScore,
		List<Award> awards
	) {
	}

	public record Award(
		String year,
		String awardName,
		String info
	) {
	}

	public record Gym(
		String gymName,
		String gymAddress,
		Double gymX,
		Double gymY,
		String gymOpen,
		String gymClose,
		Double gymScore,
		List<String> images
	) {
	}
}
