package org.helloworld.gymmate.domain.gym.gymProduct.repository;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymProductRepository extends JpaRepository<GymProduct, Long> {

	List<GymProduct> findByPartnerGym(PartnerGym partnerGym);
}
