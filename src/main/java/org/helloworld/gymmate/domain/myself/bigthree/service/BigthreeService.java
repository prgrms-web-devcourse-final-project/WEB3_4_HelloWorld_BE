package org.helloworld.gymmate.domain.myself.bigthree.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.myself.bigthree.repository.BigthreeRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BigthreeService {
    private BigthreeRepository bigthreeRepository;

    @Transactional
    public void deleteBigthree(Long bigthreeId, Member member) {
        Bigthree existBigthree = getExistingBigthree(bigthreeId);

        validateBigthreeOwner(existBigthree, member);

        bigthreeRepository.delete(existBigthree);
    }

    private Bigthree getExistingBigthree(Long bigthreeId) {
        //기존 3대 측정 기록 가져오기
        return bigthreeRepository.findById(bigthreeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BIGTHREE_NOT_FOUND));
    }

    private void validateBigthreeOwner(Bigthree Bigthree, Member member) {
        // 본인의 3대 측정 기록이 아니면 예외 발생
        if (Bigthree.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
