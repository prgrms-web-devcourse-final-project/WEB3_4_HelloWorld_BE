package org.helloworld.gymmate.domain.myself.bigthreeaverage.scheduler;

import java.util.List;

import org.helloworld.gymmate.domain.myself.bigthreeaverage.entity.BigthreeAverage;
import org.helloworld.gymmate.domain.myself.bigthreeaverage.repository.BigthreeAverageRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BigthreeAverageScheduler {
    private final MemberRepository memberRepository;
    private final BigthreeAverageRepository bigthreeAverageRepository;

    @Scheduled(cron = "0 0 0  * * *") //매일 자정 실행
    @Transactional
    public void updateDailyBigthreeAverage() {
        // 최근 벤치, 데드리프트, 스쿼트 값이 모두 있는 일반 회원 조회
        List<Member> members = memberRepository.findAllWithRecentBigthree();

        if (members.isEmpty()) {
            log.debug("3대 평균 계산을 위한 일반 회원 데이터가 없습니다.");
            return;
        }

        BigthreeAverage average = calcaulateBigthreeAverage(members);
        bigthreeAverageRepository.save(average);

        log.debug("3대 평균 저장 완료 : bench={}, deadlift={}, squat={}, sum={}",
            average.getBenchAverage(), average.getDeadliftAverage(), average.getSquatAverage(),
            average.getSumAverage());
    }

    private BigthreeAverage calcaulateBigthreeAverage(List<Member> members) {
        double totalBench = 0, totalDeadlift = 0, totalSquat = 0;
        for (Member m : members) {
            double bench = m.getRecentBench();
            double deadlift = m.getRecentDeadlift();
            double squat = m.getRecentSquat();

            totalBench += bench;
            totalDeadlift += deadlift;
            totalSquat += squat;
        }

        int count = members.size();
        double benchAvg = roundTo2(totalBench / count);
        double deadliftAvg = roundTo2(totalDeadlift / count);
        double squatAvg = roundTo2(totalSquat / count);

        return toBigthreeAverage(benchAvg, deadliftAvg, squatAvg);
    }

    private BigthreeAverage toBigthreeAverage(double benchAvg, double deadliftAvg, double squatAvg) {
        return BigthreeAverage.builder()
            .benchAverage(benchAvg)
            .deadliftAverage(deadliftAvg)
            .squatAverage(squatAvg)
            .sumAverage(benchAvg + deadliftAvg + squatAvg)
            .build();
    }

    /** 소수점 둘째 자리까지 반올림해서 DB에 저장 */
    private double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
