package org.helloworld.gymmate.domain.myself.record.service;

import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordModifyRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    public void createRecord(RecordCreateRequest request, Member member) {
    }

    public void deleteRecord(Long recordId, Member member) {
    }

    public void modifyRecord(Long recordId, RecordModifyRequest request, Member member) {
    }

    public Page<RecordResponse> getRecords(int page, int size, Member member) {
        return null;
    }
}
