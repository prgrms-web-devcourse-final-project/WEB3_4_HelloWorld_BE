package org.helloworld.gymmate.domain.user.member.mapper;

import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;

public class MemberMapper {

	public static Member toMember(SocialProvider socialProvider, MemberRequest memberRequest) {
		return Member.builder()
			.phoneNumber(String.valueOf(memberRequest.getPhoneNumber()))
			.memberName(memberRequest.getMemberName())
			.birthday(memberRequest.getBirthday())
			.email(memberRequest.getEmail())
			.genderType(memberRequest.getGenderType())
			.socialProvider(socialProvider)
			.build();
	}

}
