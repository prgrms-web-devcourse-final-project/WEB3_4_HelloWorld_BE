package org.helloworld.gymmate.domain.user.member.mapper;

import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class MemberMapper {

	public static Member toMember(Oauth oauth) {
		return Member.builder()
			.oauth(oauth)
			.phoneNumber("010-1111-2222")
			.memberName("이름1")
			.email("aa@naver.com")
			.gender(GenderType.FEMALE)
			.birthday("2000-00-00")
			.additionalInfoCompleted(false)
			.build();
	}

}
