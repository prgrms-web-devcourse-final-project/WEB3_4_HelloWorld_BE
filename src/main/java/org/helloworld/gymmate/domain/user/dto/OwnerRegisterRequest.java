package org.helloworld.gymmate.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OwnerRegisterRequest {
	@NotNull(message = "이름을 입력해주세요.")
	private String trainerName;
	@NotNull(message = "전화번호를 입력해주세요.")
	private String phoneNumber;
	@NotNull(message = "이메일을 입력해주세요.")
	private String email;
	@NotNull(message = "성별을 골라주세요.")
	private String gender;
	@NotNull(message = "은행명을 입력해주세요.")
	private String bank;
	@NotNull(message = "계좌번호를 입력해주세요.")
	private String account;
	@NotNull(message = "사업자번호를 입력해주세요.")
	private String businessNumber;
	@NotNull(message = "개업일자를 입력해주세요.")
	private String date;
}
