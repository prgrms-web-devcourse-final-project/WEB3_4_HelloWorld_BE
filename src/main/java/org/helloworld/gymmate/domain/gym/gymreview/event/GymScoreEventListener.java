package org.helloworld.gymmate.domain.gym.gymreview.event;

import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymreview.repository.GymReviewRepository;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GymScoreEventListener {

    private final GymReviewRepository gymReviewRepository;
    private final PartnerGymService partnerGymService;

    @EventListener
    public void handleGymScoreUpdateEvent(GymScoreUpdateEvent event) {
        long partnerGymId = event.partnerGymId();

        Double avgScore = gymReviewRepository.findAverageScoreByPartnerGymId(partnerGymId);
        Gym gym = partnerGymService.getGymByPartnerGymId(partnerGymId);
        gym.updateScore(avgScore);
    }
}