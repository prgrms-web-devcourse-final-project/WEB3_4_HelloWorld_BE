package org.helloworld.gymmate.domain.user.trainer.mapper;

import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;

public class TrainerMapper {

	public static Trainer toTrainer(SocialProvider socialProvider) {
		return Trainer.builder()
			.socialProvider(socialProvider)
			.isOwner(false)
			.isCheck(false)
			.score(0.0)
			.gymId(1L)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}

	public static Trainer toOwner(SocialProvider socialProvider) {
		return Trainer.builder()
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
