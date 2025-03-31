package org.helloworld.gymmate.infra.controller;

import org.helloworld.gymmate.infra.domain.gym.SeoulGymWebClientCrawler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crawl")
@RequiredArgsConstructor
public class CrawlerController {

	private final SeoulGymWebClientCrawler seoulGymWebClientCrawler;

	@GetMapping("/gym")
	public ResponseEntity<Void> gymCrawling() {
		seoulGymWebClientCrawler.crawlSeoulGyms();
		return ResponseEntity.ok().build();
	}
}
