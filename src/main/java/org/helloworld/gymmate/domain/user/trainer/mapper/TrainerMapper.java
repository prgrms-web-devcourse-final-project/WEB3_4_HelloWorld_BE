package org.helloworld.gymmate.domain.user.trainer.mapper;

import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class TrainerMapper {

	public static Trainer toTrainer(Oauth oauth) {
		return Trainer.builder()
			.oauth(oauth)
			.trainerName("트레이너") // 테스트용으로 잠시 삽입
			.isOwner(false)
			.isCheck(false)
			.score(0.0)
			.gymId(1L)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}

	public static TrainerResponse toDto(Trainer trainer) {
		return new TrainerResponse(trainer.getTrainerId(), trainer.getTrainerName());
	}

}
