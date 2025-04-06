package org.helloworld.gymmate.domain.pt.ptProduct.repository;

import org.helloworld.gymmate.domain.pt.ptProduct.entity.PtProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PtProductRepository extends JpaRepository<PtProduct, Long> {
	
	/*
		fetchLatestProducts 
	 */

	Page<PtProduct> findAllByOrderByPtProductIdDesc(Pageable pageable);

	@Query("""
		    SELECT p FROM PtProduct p
		    JOIN Trainer t ON p.trainerId = t.trainerId
		    WHERE t.trainerName LIKE %:searchTerm%
		    ORDER BY p.ptProductId DESC
		""")
	Page<PtProduct> findByTrainerNameOrderByPtProductIdDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("""
		SELECT p FROM PtProduct p
		WHERE p.info LIKE %:searchTerm%
		ORDER BY p.ptProductId DESC
		""")
	Page<PtProduct> findByPtProductNameOrderByPtProductIdDesc(@Param("searchTerm") String searchTerm,
		Pageable pageable);

	@Query("""
		    SELECT p FROM PtProduct p
		    JOIN Trainer t ON p.trainerId = t.trainerId
		    JOIN Gym g ON t.gym.gymId = g.gymId
		    WHERE g.address LIKE %:searchTerm%
		    ORDER BY p.ptProductId DESC
		""")
	Page<PtProduct> findByGymAddressOrderByPtProductIdDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	/*
		fetchScoreSortedProducts
	 */

	@Query("""
			SELECT p FROM PtProduct p
			JOIN Trainer t ON p.trainerId = t.trainerId
			ORDER BY t.score DESC
		""")
	Page<PtProduct> findAllOrderByTrainerScoreDesc(Pageable pageable);

	@Query("""
			SELECT p FROM PtProduct p
			JOIN Trainer t ON p.trainerId = t.trainerId
			WHERE t.trainerName LIKE %:searchTerm%
			ORDER BY t.score DESC
		""")
	Page<PtProduct> findByTrainerNameOrderByScoreDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("""
		    SELECT p FROM PtProduct p
		    JOIN Trainer t ON p.trainerId = t.trainerId
		    WHERE p.info LIKE %:searchTerm%
		    ORDER BY t.score DESC
		""")
	Page<PtProduct> findByPtProductNameOrderByScoreDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("""
		    SELECT p FROM PtProduct p
		    JOIN Trainer t ON p.trainerId = t.trainerId
		    JOIN Gym g ON t.gym.gymId = g.gymId
		    WHERE g.address LIKE %:searchTerm%
		    ORDER BY t.score DESC
		""")
	Page<PtProduct> findByGymAddressOrderByScoreDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	/*
		fetchNearestProducts
	 */

	@Query(value = """
		SELECT p.*
		FROM pt_product p
		JOIN gymmate_trainer t ON p.trainer_id = t.trainer_id
		JOIN gym g ON t.gym_id = g.gym_id
		WHERE (:ptProductSearchOption = 'NONE' OR
		       (:ptProductSearchOption = 'TRAINER' AND t.trainer_name LIKE CONCAT('%', :searchTerm, '%')) OR
		       (:ptProductSearchOption = 'PTPRODUCT' AND p.info LIKE CONCAT('%', :searchTerm, '%')) OR
		       (:ptProductSearchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
		ORDER BY ST_Distance_Sphere(
		    ST_GeomFromText(CONCAT('POINT(', :y, ' ', :x, ')'), 4326), g.location
		) ASC
		""",
		countQuery = """
			SELECT COUNT(*)
			FROM pt_product p
			JOIN gymmate_trainer t ON p.trainer_id = t.trainer_id
			JOIN gym g ON t.gym_id = g.gym_id
			WHERE (:ptProductSearchOption = 'NONE' OR
			       (:ptProductSearchOption = 'TRAINER' AND t.trainer_name LIKE CONCAT('%', :searchTerm, '%')) OR
			       (:ptProductSearchOption = 'PTPRODUCT' AND p.info LIKE CONCAT('%', :searchTerm, '%')) OR
			       (:ptProductSearchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
			""",
		nativeQuery = true)
	Page<PtProduct> findNearestPtProductsWithSearch(
		@Param("x") Double x, @Param("y") Double y,
		@Param("ptProductSearchOption") String ptProductSearchOption, @Param("searchTerm") String searchTerm,
		Pageable pageable);
}
