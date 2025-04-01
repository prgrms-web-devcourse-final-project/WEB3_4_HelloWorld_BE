package org.helloworld.gymmate.common.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpatialIndexInitializer implements ApplicationRunner {
	private final SpatialIndexService spatialIndexService;

	@Override
	public void run(ApplicationArguments args) {
		try {
			spatialIndexService.createSpatialIndex(); // 트랜잭션이 제대로 관리됨
		} catch (Exception e) {
			throw new RuntimeException("Spatial Index 생성 실패", e);
		}
	}
}