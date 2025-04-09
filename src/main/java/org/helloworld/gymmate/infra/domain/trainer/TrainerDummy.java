package org.helloworld.gymmate.infra.domain.trainer;

import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.enums.AwardData;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrainerDummy {

    private final AwardRepository awardRepository;
    private final TrainerRepository trainerRepository;

    public TrainerDummy(AwardRepository awardRepository, TrainerRepository trainerRepository) {
        this.awardRepository = awardRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    public void processAwardsForTrainers(List<Trainer> trainers) {
        if (trainers.isEmpty()) {
            log.debug("저장할 트레이너 데이터가 없습니다.");
            return;
        }

        for (Trainer trainer : trainers) {
            // Enum에서 어워드 데이터 가져오기
            for (AwardData awardData : AwardData.values()) {
                // 빌더를 사용하여 Award 객체 생성
                Award award = Award.builder()
                    .awardYear(awardData.getAwardYear())
                    .awardName(awardData.getAwardName())
                    .awardInfo(awardData.getAwardInfo())
                    .trainer(trainer) // 트레이너와의 관계 설정
                    .build();

                // 어워드 저장
                awardRepository.save(award);

                // 트레이너의 어워드 리스트에 추가
                trainer.getAwards().add(award);
            }

            // 트레이너 업데이트 (어워드 리스트가 변경되었으므로)
            trainerRepository.save(trainer); //명시적 저장?
        }

        log.debug("트레이너에 대한 어워드 데이터 저장 완료!");
    }
}