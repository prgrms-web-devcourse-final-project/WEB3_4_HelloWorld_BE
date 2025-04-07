package org.helloworld.gymmate.infra.domain.gym;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.util.StringUtil;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.facility.mapper.FacilityMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.repository.GymRepository;
import org.helloworld.gymmate.infra.service.KakaoMapWebClientService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeoulGymWebClientCrawler {
	private static final int BATCH_SIZE = 500;
	private final JdbcTemplate jdbcTemplate;
	private final KakaoMapWebClientService kakaoMapWebClientService;
	private final GymRepository gymRepository;
	private final Queue<Gym> gymBatch = new ConcurrentLinkedQueue<>();

	@Async
	public CompletableFuture<Void> crawlSeoulGyms() {
		log.debug("서울 체육관 크롤링 시작! (비동기 실행)");
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		double startLat = 37.4, endLat = 37.7;
		double startLng = 126.8, endLng = 127.2;
		double step = 0.05; // 5km 단위

		for (double lat = startLat; lat <= endLat; lat += step) {
			for (double lng = startLng; lng <= endLng; lng += step) {
				double finalLng = lng;
				double finalLat = lat;
				CompletableFuture<Void> future = kakaoMapWebClientService.searchHealthClubs(lng, lat, 5000)

					.thenCompose(responses -> CompletableFuture.runAsync(() -> {
						processGyms(responses, finalLng, finalLat);
						log.debug("크롤링 진행률: (현재 위치: lat={}, lng={})", finalLat, finalLng);
					}));

				futures.add(future);
			}
		}

		// 모든 비동기 작업 완료 후 남은 데이터 저장
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
			.thenRun(() -> {
				if (!gymBatch.isEmpty()) {
					// 마지막 남은 데이터 중복 검사 후 저장
					List<Gym> gymsToCheck = List.copyOf(gymBatch);
					List<String> existingUrls = gymRepository.findExistingPlaceUrls(
						gymsToCheck.stream().map(Gym::getPlaceUrl).collect(Collectors.toList())
					);

					// 중복 제거 후 남은 데이터만 저장
					gymBatch.removeIf(gym -> existingUrls.contains(gym.getPlaceUrl()));

					// 중복 제거 후 저장
					saveGyms(List.copyOf(gymBatch));
					gymBatch.clear();
				}
			});
	}

	private void processGyms(List<Map<String, Object>> responses, double lng, double lat) {

		Set<String> placeUrls = responses.stream()
			.map(response -> (String)response.get("place_url"))
			.collect(Collectors.toSet());

		List<Gym> newGyms = responses.stream()
			.filter(response -> placeUrls.contains(response.get("place_url"))) // place_url 필터링
			.map(GymMapper::toEntity)
			.peek(gym -> gym.assignFacility(FacilityMapper.toDefaultEntity()))
			.filter(gym -> gymBatch.stream()
				.noneMatch(existingGym -> existingGym.getPlaceUrl().equals(gym.getPlaceUrl()))) // 중복 검사
			.toList();

		gymBatch.addAll(newGyms);

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
				saveFacilitiesForGyms(List.copyOf(gymBatch));
				gymBatch.clear();
			}
		}
	}

	@Transactional
	public void saveGyms(List<Gym> gyms) {
		if (gyms.isEmpty()) {
			log.debug("저장할 데이터가 없습니다.");
			return;
		}

		log.debug("총 {}개의 데이터를 저장합니다.", gyms.size());

		String sql = "INSERT INTO gym (gym_name, start_time, end_time, phone_number, is_partner, " +
			"address, location, avg_score, intro, place_url) " +
			"VALUES (?, ?, ?, ?, ?, ?, ST_PointFromText(?, 4326), ?, ?, ?)";

		try {
			jdbcTemplate.batchUpdate(sql, gyms, BATCH_SIZE, (ps, gym) -> {
				ps.setString(1, gym.getGymName());
				ps.setString(2, gym.getStartTime());
				ps.setString(3, gym.getEndTime());
				ps.setString(4, gym.getPhoneNumber());
				ps.setBoolean(5, gym.getIsPartner());
				ps.setString(6, gym.getAddress());
				ps.setString(7, StringUtil.format("POINT({} {})", gym.getLocation().getY(), gym.getLocation().getX()));
				ps.setDouble(8, gym.getAvgScore());
				ps.setString(9, gym.getIntro());
				ps.setString(10, gym.getPlaceUrl());
			});

			log.debug("데이터 저장 완료!");
		} catch (Exception e) {
			log.error("데이터 저장 중 오류 발생: ", e);
		}
	}

	@Transactional
	public void saveFacilitiesForGyms(List<Gym> gyms) {
		if (gyms.isEmpty()) {
			log.debug("저장할 Gym에 연결된 Facility 데이터가 없습니다.");
			return;
		}

		// Facility INSERT 쿼리
		String facilitySql =
			"INSERT INTO facility (parking, shower_room, in_body, locker, wifi, sports_wear, towel, sauna) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		// Gym 테이블의 facility_id 컬럼 업데이트 쿼리
		String updateGymSql = "UPDATE gym SET facility_id = ? WHERE place_url = ?";

		for (Gym gym : gyms) {
			Facility facility = gym.getFacility();
			if (facility == null) {
				continue;
			}

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(facilitySql, new String[] {"facility_id"});
				ps.setBoolean(1, false);
				ps.setBoolean(2, false);
				ps.setBoolean(3, false);
				ps.setBoolean(4, false);
				ps.setBoolean(5, false);
				ps.setBoolean(6, false);
				ps.setBoolean(7, false);
				ps.setBoolean(8, false);
				return ps;
			}, keyHolder);

			Number generatedId = keyHolder.getKey();
			if (generatedId != null) {
				jdbcTemplate.update(updateGymSql, generatedId.longValue(), gym.getPlaceUrl());
			}
		}

		log.debug("Gym에 연결된 Facility 데이터 저장 및 업데이트 완료!");
	}
}