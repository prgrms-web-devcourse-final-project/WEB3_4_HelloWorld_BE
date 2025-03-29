package org.helloworld.gymmate.domain.user.trainer.mapper;

import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class TrainerMapper {

	public static Trainer toTrainer(Oauth oauth) {
		return Trainer.builder()
			.oauth(oauth)
			.isOwner(false)
			.isCheck(false)
			.score(0.0)
			.gymId(1L)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}
	
}
