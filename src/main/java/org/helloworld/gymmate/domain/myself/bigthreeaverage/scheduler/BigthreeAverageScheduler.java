package org.helloworld.gymmate.domain.myself.bigthreeaverage.scheduler;

import java.util.List;

import org.helloworld.gymmate.domain.myself.bigthreeaverage.entity.BigthreeAverage;
import org.helloworld.gymmate.domain.myself.bigthreeaverage.repository.BigthreeAverageRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
    private final ApplicationEventPublisher eventPublisher;

    /** 매일 자정에 실행되는 스케줄러 */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void scheduledUpdateDailyBigthreeAverage() {
        log.debug("스케쥴러 실행: scheduledUpdateDailyBigthreeAverage");
        updateDailyBigthreeAverage();
    }

    /** 서버 부팅 후 ApplicationReadyEvent 발생 시 데이터가 없으면 이벤트 발행 */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (bigthreeAverageRepository.count() == 0) {
            log.debug("BigthreeAverage 데이터 없음 -> 이벤트 발행:  평균 계산 비동기 실행");
            this.eventPublisher.publishEvent(new BigthreeAverageInitEvent(this));
        }
    }

    /** 이벤트 수신: 비동기 계산 로직 실행 (별도 스레드에서 처리) */
    @Async
    @EventListener
    @Transactional
    public void handleBigthreeAverageInitEvent(BigthreeAverageInitEvent event) {
        updateDailyBigthreeAverage();
    }

    /** 평균 계산 및 저장 로직 */
    private void updateDailyBigthreeAverage() {
        List<Member> members = memberRepository.findAllWithRecentBigthree();

        if (members.isEmpty()) {
            log.debug("3대 평균 계산을 위한 일반 회원 데이터가 없습니다.");
            return;
        }

        BigthreeAverage average = calculateBigthreeAverage(members);
        bigthreeAverageRepository.save(average);

        log.debug("3대 평균 저장 완료 : bench={}, deadlift={}, squat={}, sum={}",
            average.getBenchAverage(), average.getDeadliftAverage(), average.getSquatAverage(),
            average.getSumAverage());
    }

    private BigthreeAverage calculateBigthreeAverage(List<Member> members) {
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
            .bigthreeAverageId(1L)
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

    /** 부팅 시에 이벤트로 계산을 수행하도록 하는 사용자 정의 이벤트 클래스 */
    public record BigthreeAverageInitEvent(Object source) {
    }
}
