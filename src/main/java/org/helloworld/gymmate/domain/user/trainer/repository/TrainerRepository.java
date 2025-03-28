package org.helloworld.gymmate.domain.user.trainer.repository;

import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

}
