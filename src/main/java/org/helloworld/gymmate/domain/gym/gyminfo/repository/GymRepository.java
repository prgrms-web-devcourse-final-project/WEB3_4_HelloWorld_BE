package org.helloworld.gymmate.domain.gym.gyminfo.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
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

    @Query("SELECT g FROM Gym g WHERE (:searchName IS NULL OR g.gymName LIKE CONCAT('%', :searchName, '%')) ORDER BY g.gymId DESC")
    Page<Gym> searchGymsByName(@Param("searchName") String searchName, Pageable pageable);

    @Query("""
            SELECT g
            FROM Gym g
            WHERE (:searchName IS NULL OR g.gymName LIKE CONCAT('%', :searchName, '%'))
            AND g.gymId NOT IN (
                SELECT pg.gym.gymId FROM PartnerGym pg
            )
            ORDER BY g.gymId DESC
        """)
    Page<Gym> searchAvailableGymsByName(@Param("searchName") String searchName, Pageable pageable);

    Optional<Gym> findGymByGymName(String gymName);

    @Query("SELECT g FROM Gym g WHERE (:isPartner IS NULL OR g.isPartner = :isPartner) AND g.gymName LIKE CONCAT('%', :searchTerm, '%') OR g.address LIKE CONCAT('%', :searchTerm, '%') ORDER BY g.avgScore DESC")
    Page<Gym> findAll(@Param("searchTerm") String searchTerm, Boolean isPartner, Pageable pageable);

    @Query("SELECT g FROM Gym g WHERE g.gymName LIKE CONCAT('%', :searchTerm, '%') AND (:isPartner IS NULL OR g.isPartner = :isPartner) ORDER BY g.avgScore DESC")
    Page<Gym> searchGymByGymName(String searchTerm, Boolean isPartner,
        Pageable pageable);

    @Query("SELECT g FROM Gym g WHERE g.address LIKE CONCAT('%', :searchTerm, '%') AND (:isPartner IS NULL OR g.isPartner = :isPartner) ORDER BY g.avgScore DESC")
    Page<Gym> searchGymByAddress(String searchTerm, Boolean isPartner,
        Pageable pageable);

    @Query(value = """
         SELECT g
         FROM Gym g
         WHERE ((:searchOption = 'NONE' AND (g.gymName LIKE CONCAT('%', :searchTerm, '%') OR g.address LIKE CONCAT('%', :searchTerm, '%'))) OR
               (:searchOption = 'GYM' AND g.gymName LIKE CONCAT('%', :searchTerm, '%')) OR
               (:searchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
               AND (:isPartner IS NULL OR g.isPartner = :isPartner)
               AND within(g.location, FUNCTION('ST_GeomFromText', :boundingBox, 4326)) = true
         ORDER BY FUNCTION('ST_Distance_Sphere', FUNCTION('ST_GeomFromText', CONCAT('POINT(', :y, ' ', :x, ')'), 4326), g.location) ASC
        """)
    Page<Gym> findNearByGymWithSearchAndIsPartner(@Param("x") Double x, @Param("y") Double y,
        @Param("boundingBox") String boundingBoxWkt,
        @Param("searchOption") String searchOption, @Param("searchTerm") String searchTerm,
        @Param("isPartner") Boolean isPartner, Pageable pageable);

    @Query("SELECT g FROM Gym g WHERE g.placeUrl IN :placeUrls")
    List<Gym> findAllByPlaceUrlIn(@Param("placeUrls") List<String> placeUrls);
}
