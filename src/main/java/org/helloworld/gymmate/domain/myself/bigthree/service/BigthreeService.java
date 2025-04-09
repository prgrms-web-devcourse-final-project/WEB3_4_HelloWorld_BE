package org.helloworld.gymmate.domain.myself.bigthree.service;

import java.time.LocalDate;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeStatsResponse;
import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.myself.bigthree.mapper.BigthreeMapper;
import org.helloworld.gymmate.domain.myself.bigthree.repository.BigthreeRepository;
import org.helloworld.gymmate.domain.myself.bigthreeaverage.entity.BigthreeAverage;
import org.helloworld.gymmate.domain.myself.bigthreeaverage.repository.BigthreeAverageRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BigthreeService {
    private final BigthreeRepository bigthreeRepository;
    private final MemberService memberService;
    private final BigthreeAverageRepository bigthreeAverageRepository;
    private final BigthreeMapper bigthreeMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public long createBigthree(@Valid BigthreeCreateRequest request, Long memberId) {
        Member member = getMember(memberId);

        // 날짜가 비어있으면 현재 날짜 넣기
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        return bigthreeRepository.save(BigthreeMapper.toEntity(request, member, date)).getBigthreeId();
    }

    @Transactional
    public void deleteBigthree(Long bigthreeId, Long memberId) {
        Bigthree existBigthree = getExistingBigthree(bigthreeId);

        Member member = getMember(memberId);
        validateBigthreeOwner(existBigthree, member);

        bigthreeRepository.delete(existBigthree);
    }

    @Transactional
    public void modifyBigthree(Long bigthreeId, @Valid BigthreeRequest request, Long memberId) {
        Bigthree existBigthree = getExistingBigthree(bigthreeId);

        Member member = getMember(memberId);
        validateBigthreeOwner(existBigthree, member);

        // 기존 날짜 가져오기
        LocalDate date = existBigthree.getDate();

        bigthreeRepository.save(BigthreeMapper.toEntity(request, member, date));
    }

    private Member getMember(Long memberId) {
        return memberService.findByUserId(memberId);
    }

    private Bigthree getExistingBigthree(Long bigthreeId) {
        //기존 3대 측정 기록 가져오기
        return bigthreeRepository.findById(bigthreeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.BIGTHREE_NOT_FOUND));
    }

    private void validateBigthreeOwner(Bigthree bigthree, Member member) {
        // 본인의 3대 측정 기록이 아니면 예외 발생
        if (!bigthree.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }

    //TODO: Bigthree 테이블에 저장된 기록 중 가장 최신값 회원의  recent필드에 업데이트하는 로직

    // 3대 측정 통계
    @Transactional(readOnly = true)
    public BigthreeStatsResponse getBigthreeStats(Long memberId) {
        Member member = getMember(memberId);

        if (member.getRecentBench() == null || member.getRecentDeadlift() == null || member.getRecentSquat() == null) {
            throw new BusinessException(ErrorCode.BIGTHREE_NOT_FOUND);
        }

        BigthreeAverage average = getLatestBigthreeAverage();
        int mostLevel = getMostLevel();
        double myTotal = member.getRecentBench() + member.getRecentDeadlift() + member.getRecentSquat();

        double benchRate = calculateRate(member.getRecentBench(), myTotal);
        double deadliftRate = calculateRate(member.getRecentDeadlift(), myTotal);
        double squatRate = calculateRate(member.getRecentSquat(), myTotal);

        double myAvg = myTotal / 3.0;

        return bigthreeMapper.toResponseDto(member, average, mostLevel, benchRate, deadliftRate, squatRate, myAvg);
    }

    private double calculateRate(Double myWeight, Double myTotal) {
        if (myTotal == null || myTotal == 0.0)
            return 0;

        // 비율 계산 후 소수점 둘째자리까지 반올림
        double rawRate = (myWeight / myTotal) * 100;
        return Math.round(rawRate * 100.0) / 100.0;
    }

    private BigthreeAverage getLatestBigthreeAverage() {
        // 3대 측정 평균 가져오기
        return bigthreeAverageRepository.findTopByOrderByBigthreeAverageIdDesc()
            .orElseThrow(() -> new BusinessException(ErrorCode.BIGTHREE_AVERAGE_NOT_FOUND));
    }

    private int getMostLevel() {
        // 많은 일반 회원이 속한 레벨 가져오기
        return memberRepository.findMostLevel();
    }

}

