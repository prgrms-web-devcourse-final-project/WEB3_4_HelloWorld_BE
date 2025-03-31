package org.helloworld.gymmate.domain.myself.bigthree.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.mapper.BigthreeMapper;
import org.helloworld.gymmate.domain.myself.bigthree.repository.BigthreeRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BigthreeService {
    private BigthreeRepository bigthreeRepository;

    @Transactional
    public long createBigthree(@Valid BigthreeCreateRequest request, Member member) {
        // 날짜가 비어있으면 현재 시간 넣기
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        return bigthreeRepository.save(BigthreeMapper.toEntity(request, member, date)).getBigthreeId();
    }
}
