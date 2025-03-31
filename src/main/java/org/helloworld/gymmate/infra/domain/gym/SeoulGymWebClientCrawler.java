package org.helloworld.gymmate.infra.domain.gym;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.helloworld.gymmate.infra.service.KakaoMapWebClientService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeoulGymWebClientCrawler {
	private final JdbcTemplate jdbcTemplate;
	private final KakaoMapWebClientService kakaoMapWebClientService;
	private final GymRepository gymRepository;
	private static final int BATCH_SIZE = 500;
	private final Queue<Gym> gymBatch = new ConcurrentLinkedQueue<>();

	@Async
	public CompletableFuture<Void> crawlSeoulGyms() {
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		double startLat = 37.4, endLat = 37.7;
		double startLng = 126.8, endLng = 127.2;
		double step = 0.05; // 5km 단위

		for (double lat = startLat; lat <= endLat; lat += step) {
			for (double lng = startLng; lng <= endLng; lng += step) {
				double finalLng = lng;
				double finalLat = lat;

				CompletableFuture<Void> future = kakaoMapWebClientService.searchHealthClubs(lng, lat, 5000)
					.thenAcceptAsync(responses -> {
						processGyms(responses, finalLng, finalLat);
						log.info("크롤링 진행률: (현재 위치: lat={}, lng={})" , finalLat, finalLng);
					});

				futures.add(future);
			}
		}

		// 모든 비동기 작업 완료 후 남은 데이터 저장
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
			.thenRun(() -> {
				if (!gymBatch.isEmpty()) {
					saveGyms(List.copyOf(gymBatch));
					gymBatch.clear();
				}
			});
	}

	private void processGyms(List<Map<String, Object>> responses, double lng, double lat) {
		if (responses.isEmpty()) {
			log.warn("검색 결과가 없습니다. (lng: {}, lat: {})", lng, lat);
			return;
		}

		Set<String> placeUrls = responses.stream()
			.map(response -> (String) response.get("place_url"))
			.collect(Collectors.toSet());

		if (placeUrls.isEmpty()) {
			log.warn("중복된 데이터로 인해 새로운 placeUrls가 없음!");
			return;
		}

		// 500개가 모일 때마다 기존 DB와 중복 검사 실행
		if (gymBatch.size() >= BATCH_SIZE) {
			List<Gym> gymsToCheck = List.copyOf(gymBatch);
			List<String> existingUrls = gymRepository.findExistingPlaceUrls(
				gymsToCheck.stream().map(Gym::getPlaceUrl).collect(Collectors.toList())
			);

			// 중복 제거 후 남은 데이터만 유지
			gymBatch.removeIf(gym -> existingUrls.contains(gym.getPlaceUrl()));

			// 중복 제거 후 500개 저장
			if (gymBatch.size() >= BATCH_SIZE) {
				saveGyms(List.copyOf(gymBatch));
				gymBatch.clear();
			}
		}

		// 새로운 데이터 추가
		List<Gym> newGyms = responses.stream()
			.filter(response -> placeUrls.contains(response.get("place_url")))
			.map(GymMapper::toEntity)
			.toList();

		gymBatch.addAll(newGyms);
	}

	@Transactional
	public void saveGyms(List<Gym> gyms) {
		if (gyms.isEmpty()) {
			log.info("저장할 데이터가 없습니다.");
			return;
		}

		log.info("총 {}개의 데이터를 저장합니다.", gyms.size());

		String sql = "INSERT INTO gym (gym_name, start_time, end_time, phone_number, is_partner, " +
			"address, x_feild, y_feild, avg_score, intro, place_url) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.batchUpdate(sql, gyms, BATCH_SIZE, (ps, gym) -> {
				ps.setString(1, gym.getGymName());
				ps.setString(2, gym.getStartTime());
				ps.setString(3, gym.getEndTime());
				ps.setString(4, gym.getPhoneNumber());
				ps.setBoolean(5, gym.getIsPartner());
				ps.setString(6, gym.getAddress());
				ps.setString(7, gym.getXField());
				ps.setString(8, gym.getYField());
				ps.setDouble(9, gym.getAvgScore());
				ps.setString(10, gym.getIntro());
				ps.setString(11, gym.getPlaceUrl());
			});

			log.info("데이터 저장 완료!");
		} catch (Exception e) {
			log.error("데이터 저장 중 오류 발생: ", e);
		}
	}

}