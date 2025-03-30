package org.helloworld.gymmate.domain.myself.record.dto;

import java.time.LocalDate;

public record RecordResponse(
        Long recordId,
        LocalDate date,
        String title,
        String content
) {

}
