package org.helloworld.gymmate.domain.user.trainer.trainerreview.event;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.repository.TrainerReviewRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerScoreEventListener {

    private final TrainerReviewRepository trainerReviewRepository;
    private final TrainerRepository trainerRepository;

    @EventListener
    public void handleTrainerScoreUpdateEvent(TrainerScoreUpdateEvent event) {
        Long trainerId = event.trainerId();

        Double avgScore = trainerReviewRepository.findAverageScoreByTrainerId(trainerId);
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        trainer.updateScore(avgScore);
    }
}