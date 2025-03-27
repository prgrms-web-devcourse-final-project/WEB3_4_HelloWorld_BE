package org.helloworld.gymmate.domain.user.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialProviderRepository extends JpaRepository<SocialProvider, Long> {
	Optional<SocialProvider> findByProviderId(String providerId);
}
