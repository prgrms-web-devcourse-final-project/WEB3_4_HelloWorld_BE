package org.helloworld.gymmate.infra.scheduler;

import java.time.LocalDateTime;

import org.helloworld.gymmate.infra.domain.gym.SeoulGymWebClientCrawler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymCrawlScheduler {

	private final SeoulGymWebClientCrawler seoulGymWebClientCrawler;

	// 서울 헬스장 크롤링 스케쥴러
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void scheduleCrawl() {
		log.info("Crawl task started at {}", LocalDateTime.now());
		seoulGymWebClientCrawler.crawlSeoulGyms();
	}
}