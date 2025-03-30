package org.helloworld.gymmate.domain.myself.record.service;

import org.helloworld.gymmate.domain.myself.record.dto.RecordCreateRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordModifyRequest;
import org.helloworld.gymmate.domain.myself.record.dto.RecordResponse;
import org.helloworld.gymmate.security.model.GymmateUser;
import org.helloworld.gymmate.security.model.GymmateUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    public void createRecord(RecordCreateRequest request, GymmateUser gymmateUser) {
    }

    public void deleteRecord(Long recordId, GymmateUser gymmateUser) {
    }

    public void modifyRecord(Long recordId, RecordModifyRequest request, GymmateUser gymmateUser) {
    }

    public Page<RecordResponse> getRecords(int page, int size, GymmateUserDetails userDetails) {
        return null;
    }
}
