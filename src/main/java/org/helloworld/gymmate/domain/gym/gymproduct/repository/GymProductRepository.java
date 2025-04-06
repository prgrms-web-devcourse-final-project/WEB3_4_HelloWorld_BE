package org.helloworld.gymmate.domain.gym.gymproduct.repository;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymProductRepository extends JpaRepository<GymProduct, Long> {

	List<GymProduct> findByPartnerGym(PartnerGym partnerGym);
}
