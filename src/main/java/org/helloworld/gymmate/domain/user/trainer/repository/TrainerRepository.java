package org.helloworld.gymmate.domain.user.trainer.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
	Optional<Trainer> findByOauth(Oauth oauth);

	Optional<Trainer> findByTrainerId(Long trainerId);
}
