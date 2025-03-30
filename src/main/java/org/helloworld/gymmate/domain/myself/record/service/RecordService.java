package org.helloworld.gymmate.domain.myself.record.service;

import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordModifyRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.myself.record.entity.Record;
import org.helloworld.gymmate.domain.myself.record.repository.RecordRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;

    public void createRecord(RecordCreateRequest request, Member member) {
        // 날짜가 비어있으면 현재 시간 넣기
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        Record record = Record.builder()
                .date(date)
                .title(request.title())
                .content(request.content())
                .member(member)
                .build();

        recordRepository.save(record);
    }

    public void deleteRecord(Long recordId, Member member) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR)); //TODO: 에러코드 수정

        validateRecordOwner(record, member);

        recordRepository.delete(record);
    }

    public void modifyRecord(Long recordId, RecordModifyRequest request, Member member) {
    }

    public Page<RecordResponse> getRecords(int page, int size, Member member) {
        return null;
    }

    private void validateRecordOwner(Record record, Member member) {
        if (record.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
