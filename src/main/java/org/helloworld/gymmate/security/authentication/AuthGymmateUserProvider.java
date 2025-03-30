package org.helloworld.gymmate.security.authentication;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.model.GymmateUser;
import org.springframework.stereotype.Component;

@Component
public class AuthGymmateUserProvider {
	public Member getAuthenticatedGymmateUser(GymmateUserDetails gymmateUserDetails) {
		if (gymmateUserDetails == null) {
			throw new BusinessException(ErrorCode.USER_LOGIN_REQUIRED);
		}
		Member member = gymmateUserDetails.getMember();
		if (member == null) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}
		return member;
	}
}
