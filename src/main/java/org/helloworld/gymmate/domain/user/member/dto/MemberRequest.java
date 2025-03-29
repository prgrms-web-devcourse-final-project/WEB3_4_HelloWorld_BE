package org.helloworld.gymmate.domain.user.member.dto;

import org.helloworld.gymmate.domain.user.enumerate.GenderType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

	@NotNull(message = "전화번호는 필수입니다.")
	private String phoneNumber;

	@NotNull(message = "이름은 필수입니다.")
	private String memberName;

	@NotNull(message = "이메일은 필수입니다.")
	private String email;

	@NotNull(message = "생년월일은 필수입니다.")
	private String birthday;

	@NotNull(message = "성별은 필수입니다.")
	private GenderType genderType;

}
