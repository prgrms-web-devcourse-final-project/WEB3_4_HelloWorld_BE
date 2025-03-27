package org.helloworld.gymmate.domain.user.trainer.repository;

import org.helloworld.gymmate.domain.user.trainer.model.GymmateTrainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymMateTrainerRepository extends JpaRepository<GymmateTrainer, Long> {

}
