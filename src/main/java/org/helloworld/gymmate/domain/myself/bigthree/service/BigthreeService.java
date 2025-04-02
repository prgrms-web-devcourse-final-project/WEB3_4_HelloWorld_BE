package org.helloworld.gymmate.domain.myself.bigthree.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.myself.bigthree.mapper.BigthreeMapper;
import org.helloworld.gymmate.domain.myself.bigthree.repository.BigthreeRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BigthreeService {
    private final BigthreeRepository bigthreeRepository;
    private final MemberService memberService;

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

}
