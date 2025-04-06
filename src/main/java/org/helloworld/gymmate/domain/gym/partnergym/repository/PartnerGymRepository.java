package org.helloworld.gymmate.domain.gym.partnergym.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartnerGymRepository extends JpaRepository<PartnerGym, Long> {

	boolean existsByOwnerIdAndGym_GymId(Long ownerId, Long gymId);

	@Query("""
			SELECT DISTINCT pg FROM PartnerGym pg
			JOIN FETCH pg.gym g
			LEFT JOIN FETCH g.facility
			WHERE pg.partnerGymId = :partnerGymId
		""")
	Optional<PartnerGym> findByIdWithGymAndProducts(@Param("partnerGymId") Long partnerGymId);

	Optional<PartnerGym> findByOwnerId(Long ownerId);
}
