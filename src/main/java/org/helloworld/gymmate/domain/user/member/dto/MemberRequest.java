package org.helloworld.gymmate.domain.user.member.dto;

import jakarta.validation.constraints.NotNull;

public record MemberRequest(

	@NotNull(message = "전화번호는 필수입니다.")
	String phoneNumber,

	@NotNull(message = "이름은 필수입니다.")
	String memberName,

	@NotNull(message = "이메일은 필수입니다.")
	String email,

	@NotNull(message = "생년월일은 필수입니다.")
	String birthday,

	@NotNull(message = "성별은 필수입니다.")
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

	//계정잠김
	Boolean isAccountNonLocked,

	Long cash

) {
}
