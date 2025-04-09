package org.helloworld.gymmate.dummy.controller;

import org.helloworld.gymmate.dummy.service.DummyDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyDataController {
    private final DummyDataService dummyDataService;

    @GetMapping
    public ResponseEntity<?> insertDummyData() {
        dummyDataService.generateDummyData();
        return ResponseEntity.ok("더미 데이터 생성 완료.");
    }

    @GetMapping("/partner")
    public ResponseEntity<?> insertPartnerGym() {
        dummyDataService.generatePartnerGyms();
        return ResponseEntity.ok("파트너 체육관 더미 데이터 생성 완료.");
    }
}
