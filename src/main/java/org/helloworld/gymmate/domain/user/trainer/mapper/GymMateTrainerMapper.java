package org.helloworld.gymmate.domain.user.trainer.mapper;

import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.helloworld.gymmate.domain.user.trainer.model.GymmateTrainer;

public class GymMateTrainerMapper {

	public static GymmateTrainer toTrainer(SocialProvider socialProvider) {
		return GymmateTrainer.builder()
			.socialProvider(socialProvider)
			.isOwner(false)
			.isCheck(false)
			.score(0.0)
			.gymId(1L)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}

	public static GymmateTrainer toOwner(SocialProvider socialProvider) {
		return GymmateTrainer.builder()
			.socialProvider(socialProvider)
			.isOwner(true)
			.isCheck(true)
			.score(0.0)
			.gymId(1L)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}
}
