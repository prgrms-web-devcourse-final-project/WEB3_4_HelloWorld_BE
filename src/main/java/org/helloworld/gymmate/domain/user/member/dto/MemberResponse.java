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

	String xField,

	String yField,

	//3대
	Double recentBench,

	Double recentDeadlift,

	Double recentSquat,

	Integer level,

	//계정잠김
	Boolean isAccountNonLocked,

	Long cash

) {
}
