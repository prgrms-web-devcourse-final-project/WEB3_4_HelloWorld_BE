package org.helloworld.gymmate.domain.myself.record.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.myself.record.entity.Record;
import org.helloworld.gymmate.domain.myself.record.mapper.RecordMapper;
import org.helloworld.gymmate.domain.myself.record.repository.RecordRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {
    private final RecordRepository recordRepository;
    private final MemberService memberService;

    @Transactional
    public Long createRecord(RecordCreateRequest request, Long memberId) {
        Member member = getMember(memberId);

        // 날짜가 비어있으면 현재 날짜 넣기
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        return recordRepository.save(RecordMapper.toEntity(request, member, date)).getRecordId();
    }

    @Transactional
    public void deleteRecord(Long recordId, Long memberId) {
        Member member = getMember(memberId);
        Record existRecord = getExistingRecord(recordId);

        validateRecordOwner(existRecord, member);

        recordRepository.delete(existRecord);
    }

    @Transactional
    public Long modifyRecord(Long recordId, RecordRequest request, Long memberId) {
        Member member = getMember(memberId);
        Record existRecord = getExistingRecord(recordId);

        validateRecordOwner(existRecord, member);

        // 기존 날짜 가져오기
        LocalDate date = existRecord.getDate();

        return recordRepository.save(RecordMapper.toEntity(request, member, date)).getRecordId();
    }

    public Page<RecordResponse> getRecords(int page, int size, Long memberId) {
        Member member = getMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<Record> recordPage = recordRepository.findByMember(member, pageable);

        return recordPage.map(RecordMapper::toDto);
    }

    private Member getMember(Long memberId) {
        return memberService.findByUserId(memberId);
    }

    private Record getExistingRecord(Long recordId) {
        //기존 운동 기록 가져오기
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
    }

    private void validateRecordOwner(Record record, Member member) {
        // 본인의 운동 기록이 아니면 예외 발생
        if (!record.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
