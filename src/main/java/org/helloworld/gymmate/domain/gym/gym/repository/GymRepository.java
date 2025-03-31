package org.helloworld.gymmate.domain.gym.gym.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
	@EntityGraph(attributePaths = "images")
	Optional<Gym> findWithImagesByGymId(Long gymId);

	@Query("SELECT g.placeUrl FROM Gym g WHERE g.placeUrl IN :placeUrls")
	List<String> findExistingPlaceUrls(List<String> placeUrls);
}
