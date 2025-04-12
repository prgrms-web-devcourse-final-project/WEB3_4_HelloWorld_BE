package org.helloworld.gymmate.domain.user.trainer.trainerreview.event;

import org.helloworld.gymmate.common.s3.FileManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainerReviewDeleteEventListener {
    private final FileManager fileManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTrainerReviewDeletion(TrainerReviewDeleteEvent event) {
        event.images().forEach(image -> {
            try {
                fileManager.deleteFile(image.getUrl());
            } catch (Exception ex) {
                log.error("image Url : {} , error : {} ", image.getUrl(), ex.getMessage());
            }
        });
    }
}
