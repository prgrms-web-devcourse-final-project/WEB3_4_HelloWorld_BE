package org.helloworld.gymmate.domain.gym.gymproduct.repository;

import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymProductRepository extends JpaRepository<GymProduct, Long> {
}
