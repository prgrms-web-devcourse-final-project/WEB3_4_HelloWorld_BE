package org.helloworld.gymmate.security.authentication;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.model.GymmateUser;
import org.springframework.stereotype.Component;

@Component
public class AuthGymmateUserProvider {
	public GymmateUser getAuthenticatedGymmateUser(GymmateUserDetails gymmateUserDetails) {
		if (gymmateUserDetails == null) {
			throw new BusinessException(ErrorCode.USER_LOGIN_REQUIRED);
		}
		GymmateUser gymateUser = gymmateUserDetails.getGymmateUser();
		if (gymateUser == null) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}
		return gymateUser;
	}
}
