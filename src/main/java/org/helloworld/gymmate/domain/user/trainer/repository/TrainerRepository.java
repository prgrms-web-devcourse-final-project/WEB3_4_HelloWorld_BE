package org.helloworld.gymmate.domain.user.trainer.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
	List<Trainer> findByTrainerIdIn(Set<Long> trainerIds);

	Optional<Trainer> findByOauth(Oauth oauth);

	Optional<Trainer> findByTrainerId(Long trainerId);

}
