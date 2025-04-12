package org.helloworld.gymmate.domain.user.member.mapper;

import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberResponse;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class MemberMapper {

    public static Member toMember(Oauth oauth) {
        return Member.builder()
            .oauth(oauth)
            .cash(5000L)
            .additionalInfoCompleted(false)
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
            member.getProfileUrl(),
            member.getCash()
        );
    }

    public static MemberCheckResponse toCheckResponse(Member member) {
        return new MemberCheckResponse(
            UserType.MEMBER.toString()
        );
    }
}
