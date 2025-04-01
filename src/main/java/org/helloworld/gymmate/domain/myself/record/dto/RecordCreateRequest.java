package org.helloworld.gymmate.domain.myself.record.dto;

import java.time.LocalDate;

public record RecordCreateRequest(
        LocalDate date, // 생략 가능
        RecordRequest recordRequest
) {

}
