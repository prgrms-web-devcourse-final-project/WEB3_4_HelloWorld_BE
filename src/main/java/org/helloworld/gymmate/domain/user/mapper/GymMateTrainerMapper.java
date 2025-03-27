package org.helloworld.gymmate.domain.user.mapper;

import org.helloworld.gymmate.domain.user.model.GymmateTrainer;
import org.helloworld.gymmate.domain.user.model.SocialProvider;

public class GymMateTrainerMapper {

	public static GymmateTrainer toTrainer(SocialProvider socialProvider) {
		return GymmateTrainer.builder()
			.socialProvider(socialProvider)
			.isOwner(false)
			.isCheck(false)
			.businessNumber(null)
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
