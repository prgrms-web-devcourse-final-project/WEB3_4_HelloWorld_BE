package org.helloworld.gymmate.domain.myself.bigthree.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record BigthreeRequest(
    @PositiveOrZero double bench,
    @PositiveOrZero double deadlift,
    @PositiveOrZero double squat
) {

}
