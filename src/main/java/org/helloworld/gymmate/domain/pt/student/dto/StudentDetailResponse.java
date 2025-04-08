package org.helloworld.gymmate.domain.pt.student.dto;

public record StudentDetailResponse(
	String name,
	String progress,
	String memo,
	String profileUrl,
	String gender,
	String height,
	String weight,
	Double bench,
	Double deadlift,
	Double squat,
	Integer level
) {
}