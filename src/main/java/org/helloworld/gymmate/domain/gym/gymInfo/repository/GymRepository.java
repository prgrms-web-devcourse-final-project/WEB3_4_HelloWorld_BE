package org.helloworld.gymmate.domain.gym.gymInfo.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
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

	@Query(value = "SELECT g.* FROM gym g " +
		"WHERE ST_Distance_Sphere(g.location, ST_PointFromText(:point, 4326)) <= :distance " +
		"ORDER BY ST_Distance_Sphere(g.location, ST_PointFromText(:point, 4326)) " +
		"LIMIT :limit", nativeQuery = true)
	List<Gym> findNearbyGyms(@Param("point") String point, @Param("distance") double distance,
		@Param("limit") int limit);

	Page<Gym> findAllByIsPartnerOrderByAvgScoreDesc(Boolean isPartner, Pageable pageable);

	Page<Gym> findByGymNameContainingIgnoreCaseAndIsPartnerOrderByAvgScoreDesc(String searchTerm, Boolean isPartner,
		Pageable pageable);

	@Query("SELECT g FROM Gym g WHERE g.address LIKE CONCAT('%', :searchTerm, '%') AND g.isPartner = :isPartner")
	Page<Gym> findByAddressAndIsPartnerOrderByAvgScoreDesc(String searchTerm, Boolean isPartner,
		Pageable pageable);

	@Query(value = """
		 SELECT g.*
		 FROM gym g
		 WHERE (:searchOption = 'NONE' OR
		       (:searchOption = 'GYM' AND g.gym_name LIKE CONCAT('%', :searchTerm, '%')) OR
		       (:searchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
				 AND g.is_partner = :isPartner
		 ORDER BY ST_Distance_Sphere(
		 		    ST_GeomFromText(CONCAT('POINT(', :y, ' ', :x, ')'), 4326), g.location 		
		 ) ASC
		""",
		countQuery = """
			SELECT COUNT(*)
			FROM gym g
			  WHERE (:searchOption = 'NONE' OR
				    (:searchOption = 'GYM' AND g.gym_name LIKE CONCAT('%', :searchTerm, '%')) OR
				    (:searchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
			""", nativeQuery = true)
	Page<Gym> findNearByGymWithSearchAndIsPartner(@Param("x") Double x, @Param("y") Double y,
		@Param("searchOption") String searchOption, @Param("searchTerm") String searchTerm,
		@Param("isPartner") Boolean isPartner, Pageable pageable);

}
