package org.helloworld.gymmate.security.oauth.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.rq.Rq;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.model.SocialUserInfo;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final OauthService oauthService;
	private final MemberService memberService;
	private final TrainerService trainerService;
	private final Rq rq;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 기본 사용자 정보 로드
		OAuth2User oAuth2User = super.loadUser(userRequest);
		return processOAuth2User(userRequest, oAuth2User);
	}

	private CustomOAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		// 공급자 이름 추출 (예: "kakao")
		String provider = userRequest.getClientRegistration().getRegistrationId();
		// 공급자로부터 받은 전체 사용자 정보
		Map<String, Object> attributes = oAuth2User.getAttributes();
		SocialUserInfo socialUserInfo = new SocialUserInfo(attributes);
		String providerId = socialUserInfo.getProviderId();
		String state = rq.getParameter("state");
		log.debug("state : {}", state);

		UserType userType = UserType.fromString(state);

		// oauth 엔티티 조회 또는 신규 생성
		Oauth oauth = oauthService.findOauthByProviderIdAndUserType(providerId, userType)
			.orElseGet(() -> oauthService.createOauth(providerId, userType));

		Long userId = 0L;
		// 가입 유형에 따른 사용자 등록 처리
		if (userType == UserType.TRAINER) {
			userId = trainerService.getTrainerIdByOauth(oauth.getProviderId())
				.orElseGet(() -> {
					log.debug("신규 트레이너입니다. 등록을 진행합니다.");
					return trainerService.createTrainer(
						oauth
					);
				});
		}
		if (userType == UserType.MEMBER) {
			userId = memberService.getMemberIdByOauth(oauth.getProviderId())
				.orElseGet(() -> {
					log.debug("신규 유저입니다. 등록을 진행합니다.");
					return memberService.createMember(
						oauth
					);
				});
		}
		return new CustomOAuth2User(oAuth2User, userId, userType);
	}

	public CustomOAuth2User loadUserByUserId(Long userId, String userTypeStr) {
		UserType userType = UserType.fromString(userTypeStr);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("userId", userId);
		attributes.put("userType", userTypeStr);
		//  권한 부여 (ROLE_MEMBER 또는 ROLE_TRAINER)
		Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userTypeStr));

		Trainer trainer = trainerService.findByUserId(userId);

		OAuth2User dummyOAuth2User = new OAuth2User() {
			@Override
			public Map<String, Object> getAttributes() {
				return attributes;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return authorities;
			}

			@Override
			public String getName() {
				return trainer.getTrainerName();
			}
		};

		return new CustomOAuth2User(dummyOAuth2User, userId, userType);
	}
}