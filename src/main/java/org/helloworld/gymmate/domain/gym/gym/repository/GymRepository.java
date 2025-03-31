package org.helloworld.gymmate.domain.gym.gym.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
	@EntityGraph(attributePaths = "images")
	Optional<Gym> findWithImagesByGymId(Long gymId);

	@Query("SELECT g.placeUrl FROM Gym g WHERE g.placeUrl IN :placeUrls")
	List<String> findExistingPlaceUrls(List<String> placeUrls);

	@Query("""
				SELECT g
				FROM Gym g
				WHERE g.gymName LIKE CONCAT('%', :searchName, '%')
				ORDER BY g.gymId DESC
		""")
	Page<Gym> searchGymsByName(@Param("searchName") String searchName, Pageable pageable);

	Optional<Gym> findGymByGymName(String gymName);

}
