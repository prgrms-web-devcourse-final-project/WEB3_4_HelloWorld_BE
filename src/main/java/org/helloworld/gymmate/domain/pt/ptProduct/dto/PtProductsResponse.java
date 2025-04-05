package org.helloworld.gymmate.domain.pt.ptProduct.dto;

public record PtProductsResponse(
	Long ptProductId,
	String productName,
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
	) {
	}
}