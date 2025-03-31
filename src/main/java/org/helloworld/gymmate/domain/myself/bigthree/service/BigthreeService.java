package org.helloworld.gymmate.domain.myself.bigthree.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
}
