package org.helloworld.gymmate.domain.gym.facility.repository;

import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
