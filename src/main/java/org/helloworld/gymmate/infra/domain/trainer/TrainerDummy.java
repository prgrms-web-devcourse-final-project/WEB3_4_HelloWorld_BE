package org.helloworld.gymmate.infra.domain.trainer;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.enums.AwardData;
import org.helloworld.gymmate.domain.user.trainer.award.mapper.AwardMapper;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TrainerDummy {

    private static final int BATCH_SIZE = 500;
    private final JdbcTemplate jdbcTemplate;
    private final AwardRepository awardRepository;
    private final TrainerRepository trainerRepository;

    @Transactional
    public void processAwardsForTrainers(List<Trainer> trainers) {
        if (trainers.isEmpty()) {
            log.debug("저장할 트레이너 데이터가 없습니다.");
            return;
        }
        //db에 insert할 award 리스트
        List<Award> awardsToInsert = new ArrayList<>();

        //트레이너마다 랜덤한 award 3개 생성
        for (Trainer trainer : trainers) {
            // Enum에서 AwardData3개 가져오기
            List<AwardData> selectedAwards = AwardData.getRandomAwards(3);

            for (AwardData awardData : selectedAwards) { // 각 어워드마다

                Award award = AwardMapper.toEntity(awardData, trainer.getTrainerId()); // Award 객체 생성

                awardsToInsert.add(award); // 리스트에 어워드 저장

                //awardRepository.save(award);

                // 트레이너의 어워드 리스트에 추가
                trainer.getAwards().add(award);
            }
        }

        // award INSERT 쿼리 ( year, name, info, trainer 정보 )
        String sql =
            "Insert into award (award_year, award_name, award_info, trainer_id) "
                + "values (?, ?, ? ,?)";
        try {
            jdbcTemplate.batchUpdate(sql, awardsToInsert, BATCH_SIZE, (ps, award) -> {
                //awardsToInsert의 award객체를 꺼내서 sql의 '?'부분의 값 설정
                ps.setString(1, award.getAwardYear());
                ps.setString(2, award.getAwardName());
                ps.setString(3, award.getAwardInfo());
                ps.setLong(4, award.getTrainerId());
            });

            log.debug("총 {}개의 award 데이터 저장 완료!", awardsToInsert.size());

        } catch (Exception e) {

            log.error("award 데이터 저장 중 오류 발생: ", e);
        }

    }
}