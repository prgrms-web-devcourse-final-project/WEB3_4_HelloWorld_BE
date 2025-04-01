package org.helloworld.gymmate.security.oauth.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthRepository extends JpaRepository<Oauth, Long> {
	Optional<Oauth> findByProviderIdAndUserType(String providerId, UserType userType);
}
