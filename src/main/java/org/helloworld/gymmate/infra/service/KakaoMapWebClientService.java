package org.helloworld.gymmate.infra.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMapWebClientService {

	private final WebClient.Builder webClientBuilder;
	private WebClient webClient;

	@Value("${kakao.api.key}")
	private String kakaoApiKey;

	private final Executor executor = Executors.newFixedThreadPool(10);

	@PostConstruct
	public void init() {
		this.webClient = webClientBuilder.baseUrl("https://dapi.kakao.com").build();
	}

	@Async
	public CompletableFuture<List<Map<String, Object>>> searchHealthClubs(double x, double y, int radius) {
		List<CompletableFuture<List<Map<String, Object>>>> futures = new ArrayList<>();
		AtomicBoolean stopFlag = new AtomicBoolean(false); // 중단 여부를 관리하는 플래그

		for (int page = 1; page <= 45; page++) {
			if (stopFlag.get()) break;
			futures.add(fetchPage(x, y, radius, page, stopFlag));
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
			.thenApply(v -> futures.stream()
				.map(CompletableFuture::join)
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(
					item -> (String) item.get("place_url"),
					Function.identity(),
					(existing, replacement) -> existing
				))
				.values()
				.stream()
				.toList());
	}

	private CompletableFuture<List<Map<String, Object>>> fetchPage(double x, double y, int radius, int page, AtomicBoolean stopFlag) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(getRandomDelay()); // 랜덤 딜레이 추가
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			if (stopFlag.get()) return Collections.emptyList(); // 중단 플래그 체크

			return webClient.get()
				.uri(uriBuilder -> uriBuilder
					.path("/v2/local/search/keyword.json")
					.queryParam("query", "헬스장")
					.queryParam("x", x)
					.queryParam("y", y)
					.queryParam("radius", radius)
					.queryParam("size", 15)
					.queryParam("page", page)
					.build())
				.header("Authorization", "KakaoAK " + kakaoApiKey)
				.exchangeToMono(response -> {
					HttpStatusCode status = response.statusCode();
					// ✅ 응답 코드 확인 (2xx가 아닌 경우)
					if (!status.is2xxSuccessful()) {
						log.error("Kakao API 응답 실패 (page={}, lat={}, lng={}, status={})", page, y, x, status);
						return Mono.just(Collections.emptyMap());
					}

					return response.bodyToMono(Map.class);
				})
				.blockOptional()
				.map(responseMap -> {
					List<Map<String, Object>> documents = (List<Map<String, Object>>) responseMap.getOrDefault("documents", Collections.emptyList());

					Object metaObj = responseMap.get("meta");
					if (metaObj instanceof Map) {
						Map<String, Object> meta = (Map<String, Object>) metaObj;
						boolean isEnd = (boolean) meta.getOrDefault("is_end", false);
						if (isEnd) {
							stopFlag.set(true); // is_end 감지되면 중단 플래그 설정
						}
					}

					return documents;
				})
				.orElse(Collections.emptyList());

		}, executor);
	}
	// 랜덤 딜레이 (500ms ~ 2000ms)
	private long getRandomDelay() {
		Random random = new Random();
		return 500 + random.nextInt(1501);
	}
}