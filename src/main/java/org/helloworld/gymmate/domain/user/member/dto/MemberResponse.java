package org.helloworld.gymmate.domain.user.member.dto;

public record MemberResponse(

	String phoneNumber,

	String memberName,

	String email,

	String birthday,

	String gender,
	//신체정보
	String height,

	String weight,

	//주소
	String address,

	Double xField,

	Double yField,

	//3대
	Double recentBench,

	Double recentDeadlift,

	Double recentSquat,

	Integer level,

	String profileUrl,

	Long cash

) {
}
