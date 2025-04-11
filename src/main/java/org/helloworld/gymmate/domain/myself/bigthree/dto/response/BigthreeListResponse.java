package org.helloworld.gymmate.domain.myself.bigthree.dto.response;

import java.time.LocalDate;

public record BigthreeListResponse(
    Long bigthreeId,
    LocalDate date,
    int sum
) {
}
