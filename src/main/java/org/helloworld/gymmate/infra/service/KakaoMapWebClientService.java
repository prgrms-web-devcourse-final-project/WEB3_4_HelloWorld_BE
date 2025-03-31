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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

		for (int page = 1; page <= 45; page++) {
			futures.add(fetchPage(x, y, radius, page));
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

	private CompletableFuture<List<Map<String, Object>>> fetchPage(double x, double y, int radius, int page) {
		CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
		CompletableFuture.runAsync(() -> {
			try {
				// 랜덤 딜레이 추가 (0.5초에서 2초 사이 랜덤)
				Thread.sleep(getRandomDelay());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, executor).thenRun(() -> {
			webClient.get()
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
				.retrieve()
				.bodyToMono(Map.class)
				.map(response -> (List<Map<String, Object>>) response.get("documents"))
				.onErrorReturn(Collections.emptyList())
				.subscribe(future::complete, future::completeExceptionally);
		});

		return future;
	}
	// 랜덤 딜레이 (500ms ~ 1000ms)
	private long getRandomDelay() {
		Random random = new Random();
		return 500 + random.nextInt(501);
	}
}