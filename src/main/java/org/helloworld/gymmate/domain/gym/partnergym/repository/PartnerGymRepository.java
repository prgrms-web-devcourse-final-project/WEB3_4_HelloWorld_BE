package org.helloworld.gymmate.domain.gym.partnergym.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.partnergym.dao.PartnerGymNameProjection;
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

    @Query("SELECT pg.partnerGymId AS partnerGymId, pg.gym.gymName AS gymName FROM PartnerGym pg WHERE pg.partnerGymId IN :ids")
    List<PartnerGymNameProjection> findGymNamesByPartnerGymIds(@Param("ids") Collection<Long> ids);

    PartnerGym findByGym_GymId(Long gymId);
    @Query("SELECT pg.partnerGymId AS partnerGymId, pg.gym.gymName AS gymName FROM PartnerGym pg WHERE pg.partnerGymId IN :ids")
    List<PartnerGymNameProjection> findGymNamesByPartnerGymIds(@Param("ids") Collection<Long> ids);

    Optional<PartnerGym> findByGym_GymId(Long gymId);
}
