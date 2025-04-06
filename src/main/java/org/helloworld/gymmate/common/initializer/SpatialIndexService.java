package org.helloworld.gymmate.common.initializer;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SpatialIndexService {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void createSpatialIndex() {
		// 기존 인덱스가 있는지 확인
		String checkIndexQuery = "SHOW INDEX FROM gym WHERE Key_name = 'idx_gym_location'";

		List<?> result = entityManager.createNativeQuery(checkIndexQuery).getResultList();
		if (!result.isEmpty()) {
			log.debug("Spatial Index already exists, skipping creation.");
			return; // 이미 존재하면 생성하지 않음
		}

		// 인덱스 생성
		entityManager.createNativeQuery(
			"CREATE SPATIAL INDEX idx_gym_location ON gym(location)"
		).executeUpdate();
	}
}