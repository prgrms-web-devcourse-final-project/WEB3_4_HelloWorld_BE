package org.helloworld.gymmate.domain.user.member.mapper;

import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberResponse;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.entity.MemberProfile;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class MemberMapper {

	public static Member toMember(Oauth oauth) {
		return Member.builder()
			.oauth(oauth)
			.phoneNumber("010-1111-2222")
			.memberName("이름1")
			.email("aa@naver.com")
			.genderType(GenderType.FEMALE)
			.birthday("2000-00-00")
			.additionalInfoCompleted(false)
			.build();
	}

	// 프로필 이미지 Mapper
	public static MemberProfile toEntity(String imageUrl, Member member) {
		return MemberProfile.builder()
			.url(imageUrl)
			.member(member)
			.build();
	}

	public static MemberResponse toResponseDto(Member member) {
		return new MemberResponse(
			member.getPhoneNumber(),
			member.getMemberName(),
			member.getEmail(),
			member.getBirthday(),
			member.getGenderType().toString(),
			member.getHeight(),
			member.getWeight(),
			member.getAddress(),
			member.getXField(),
			member.getYField(),
			member.getRecentBench(),
			member.getRecentDeadlift(),
			member.getRecentSquat(),
			member.getLevel(),
			member.getIsAccountNonLocked(),
			member.getCash()
		);
	}

	public static MemberCheckResponse toCheckResponse(Member member) {
		return new MemberCheckResponse(
			UserType.MEMBER.toString()
		);
	}
}
