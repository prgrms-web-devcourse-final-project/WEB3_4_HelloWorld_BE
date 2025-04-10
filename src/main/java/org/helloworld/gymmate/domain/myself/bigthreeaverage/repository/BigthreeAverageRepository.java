package org.helloworld.gymmate.domain.myself.bigthreeaverage.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.myself.bigthreeaverage.entity.BigthreeAverage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigthreeAverageRepository extends JpaRepository<BigthreeAverage, Long> {
    /** BigthreeAverage 조회, 항상 id = 1로 요청해야 함 */
    Optional<BigthreeAverage> findByBigthreeAverageId(Long id);
}
