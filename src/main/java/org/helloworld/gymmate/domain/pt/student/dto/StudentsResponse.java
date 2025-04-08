package org.helloworld.gymmate.domain.pt.student.dto;

public record StudentsResponse(
	String name,
	String progress,
	String memo,
	String profileUrl
) {
}