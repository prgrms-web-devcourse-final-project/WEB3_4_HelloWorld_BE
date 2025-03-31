package org.helloworld.gymmate.domain.pt.pt_product.dto;

public record PtProductsResponse(
	Long ptProductId,
	PtTrainerResponse trainer,
	String info,
	Long ptProductFee
) {
	public record PtTrainerResponse(
		Long trainerId,
		String trainerName,
		String gender,
		String profile,
		Double score
	) {}
}