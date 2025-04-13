package org.helloworld.gymmate.dummy.controller;

import org.helloworld.gymmate.dummy.service.DummyDataService;
import org.helloworld.gymmate.dummy.trainer.TrainerDummyCreate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Profile("prod") // prod 환경에서만 컨트롤러 활성화
@Tag(name = "더미 데이터 삽입 API", description = "더미 데이터 삽입용 api")
@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyDataController {
    private final DummyDataService dummyDataService;
    private final TrainerDummyCreate trainerDummyCreate;

    @Operation(summary = "[첫번째 실행] 트레이너 생성", description = "크롤링 후 첫번째로 실행하세요")
    @GetMapping
    public ResponseEntity<?> insertDummyData() {
        dummyDataService.generateDummyData();
        return ResponseEntity.ok("더미 데이터 생성 완료.");
    }

    @Operation(summary = "[두번째 실행] 제휴 헬스장 생성", description = "크롤링 후 두번째로 실행하세요")
    @GetMapping("/partner")
    public ResponseEntity<?> insertPartnerGym() {
        dummyDataService.generatePartnerGyms();
        return ResponseEntity.ok("파트너 체육관 더미 데이터 생성 완료.");
    }

    @Operation(summary = "[세번째 실행] 트레이너 부가 정보 생성", description = "크롤링 후 세번째로 실행하세요")
    @GetMapping("/trainer-detail")
    public ResponseEntity<?> insertTrainerDetails() {
        trainerDummyCreate.createTrainerDummy();
        return ResponseEntity.ok("트레이너 부가 정보 더미 데이터 생성 완료.");
    }

    @Operation(summary = "owner의 gymId 업데이트")
    @GetMapping("/trainer-gym")
    public ResponseEntity<?> insertOwnerGym() {
        dummyDataService.setGymForOwner();
        return ResponseEntity.ok("트레이너의 gym정보 업데이트");
    }
}
