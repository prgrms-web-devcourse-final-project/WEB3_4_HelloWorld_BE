package org.helloworld.gymmate.domain.myself.bigthree.dto.request;

import java.time.LocalDate;

public record BigthreeCreateRequest(
    LocalDate date, // 생략 가능
    BigthreeRequest bigthreeRequest
) {

}
