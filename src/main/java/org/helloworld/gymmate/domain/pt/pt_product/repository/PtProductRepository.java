package org.helloworld.gymmate.domain.pt.pt_product.repository;

import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
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
	Page<PtProduct> findByPtProductNameOrderByPtProductIdDesc(@Param("searchTerm") String searchTerm, Pageable pageable);
	
	@Query("""
        SELECT p FROM PtProduct p
        JOIN Trainer t ON p.trainerId = t.trainerId
        JOIN Gym g ON t.gymId = g.gymId
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
        JOIN Gym g ON t.gymId = g.gymId
        WHERE g.address LIKE %:searchTerm%
        ORDER BY t.score DESC
    """)
	Page<PtProduct> findByGymAddressOrderByScoreDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

	/*
		fetchNearestProducts
	 */
}
