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
    private final MemberRepository memberRepository;

    /** 3대 기록 생성 요청 처리 */
    @Transactional
    public long createBigthree(@Valid BigthreeCreateRequest request, Long memberId) {
        Member member = getMember(memberId);

        // 날짜가 비어있으면 현재 날짜 넣기
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        validateBigthreeNotExistsDate(member, date);

        // 새 3대 기록 생성 및 저장
        Bigthree newBigthree = BigthreeMapper.toEntity(request, member, date);
        bigthreeRepository.save(newBigthree);

        updateMemberRecentBigthree(member);

        return newBigthree.getBigthreeId();
    }

    /** 3대 기록 삭제 요청 처리 */
    @Transactional
    public void deleteBigthree(Long bigthreeId, Long memberId) {
        Bigthree existBigthree = getExistingBigthree(bigthreeId);

        Member member = getMember(memberId);
        validateBigthreeOwner(existBigthree, member);

        bigthreeRepository.delete(existBigthree);

        updateMemberRecentBigthree(member);
    }

    /** 3대 기록 수정 요청 처리 */
    @Transactional
    public Long modifyBigthree(Long bigthreeId, @Valid BigthreeRequest request, Long memberId) {
        Bigthree existBigthree = getExistingBigthree(bigthreeId);

        Member member = getMember(memberId);
        validateBigthreeOwner(existBigthree, member);

        // 기존 날짜 가져오기
        LocalDate date = existBigthree.getDate();

        // 기존 3대 기록 id로 업데이트
        bigthreeRepository.save(BigthreeMapper.toEntity(request, member, date, existBigthree.getBigthreeId()));

        updateMemberRecentBigthree(member);

        return existBigthree.getBigthreeId();
    }

    /** 3대 측정 통계 요청 처리 */
    @Transactional(readOnly = true)
    public BigthreeStatsResponse getBigthreeStats(Long memberId) {
        Member member = getMember(memberId);

        validateMemberRecentBigthreeExists(member);

        BigthreeAverage average = getLatestBigthreeAverage();
        int mostLevel = getMostLevel();

        double myTotal = member.getRecentBench() + member.getRecentDeadlift() + member.getRecentSquat();

        double benchRate = calculateRate(member.getRecentBench(), myTotal);
        double deadliftRate = calculateRate(member.getRecentDeadlift(), myTotal);
        double squatRate = calculateRate(member.getRecentSquat(), myTotal);

        return BigthreeMapper.toResponseDto(mostLevel, benchRate, deadliftRate, squatRate, average, member);
    }

    /** member 회원가입 시 최초 3대 기록 저장 */
    public void createInitialBigthree(Member member) {
        validateMemberRecentBigthreeExists(member);

        Bigthree bigthree = BigthreeMapper.toInitialBigthree(member);

        bigthreeRepository.save(bigthree);
    }

    private Member getMember(Long memberId) {
        return memberService.findByUserId(memberId);
    }

    /**기존 3대 측정 기록 가져오기 */
    private Bigthree getExistingBigthree(Long bigthreeId) {
        return bigthreeRepository.findById(bigthreeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.BIGTHREE_NOT_FOUND));
    }

    /** 본인의 3대 측정 기록이 아니면 예외 발생 */
    private void validateBigthreeOwner(Bigthree bigthree, Member member) {
        if (!bigthree.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }

    /** 이미 기록이 있는 날짜인지 확인 후 있으면 예외 발생 (잘못된 요청, put 으로 요청해야 함) */
    private void validateBigthreeNotExistsDate(Member member, LocalDate date) {
        bigthreeRepository.findByMemberAndDate(member, date)
            .ifPresent(b -> {
                throw new BusinessException(ErrorCode.BIGTHREE_ALREADY_EXISTS);
            });
    }

    /** Member의 최근 3대 기록이 없으면 예외 발생 (회원가입부터 문제가 있었거나 존재하던 데이터가 누락된 경우) */
    private void validateMemberRecentBigthreeExists(Member member) {
        if (member.getRecentBench() == null || member.getRecentDeadlift() == null || member.getRecentSquat() == null) {
            throw new BusinessException(ErrorCode.BIGTHREE_NOT_FOUND);
        }
    }

    /** BigThree 테이블의 최신 데이터를 기준으로 Member 업데이트 */
    private void updateMemberRecentBigthree(Member member) {
        bigthreeRepository.findTopByMemberOrderByDateDesc(member)
            .ifPresent(latest -> member.updateRecentBigthree(
                latest.getBench(),
                latest.getDeadlift(),
                latest.getSquat()
            ));
    }

    /** 선택 종목 비율 계산하기 */
    private double calculateRate(Double myWeight, Double myTotal) {
        if (myTotal == null || myTotal == 0.0)
            return 0;

        // 비율 계산 후 소수점 둘째자리까지 반올림
        double rawRate = (myWeight / myTotal) * 100;
        return Math.round(rawRate * 100.0) / 100.0;
    }

    /** 3대 측정 평균 가져오기 */
    private BigthreeAverage getLatestBigthreeAverage() {
        return bigthreeAverageRepository.findByBigthreeAverageId(1L)
            .orElseThrow(() -> new BusinessException(ErrorCode.BIGTHREE_AVERAGE_NOT_FOUND));
    }

    /** 많은 일반 회원이 속한 레벨 가져오기 */
    private int getMostLevel() {
        return memberRepository.findMostLevel();
    }
}

