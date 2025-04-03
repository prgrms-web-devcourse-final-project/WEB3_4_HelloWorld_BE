package org.helloworld.gymmate.domain.user.trainer.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerCheckResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerListResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class TrainerMapper {

	public static Trainer toTrainer(Oauth oauth) {
		return Trainer.builder()
			.oauth(oauth)
			.isOwner(false)
			.isCheck(false)
			.score(0.0)
			.cash(100000000L)
			.additionalInfoCompleted(false)
			.build();
	}

	public static TrainerResponse toResponse(Trainer trainer) {
		return new TrainerResponse(trainer.getTrainerId(),
			trainer.getTrainerName(),
			trainer.getBank(),
			trainer.getAccount(),
			trainer.getGenderType().toString(),
			trainer.getProfile(),
			trainer.getIsOwner(),
			trainer.getCash(),
			trainer.getIntro(),
			trainer.getCareer(),
			trainer.getField());
	}

	public static TrainerCheckResponse toCheckResponse(Trainer trainer) {
		return new TrainerCheckResponse(
			UserType.TRAINER.toString(),
			trainer.getIsOwner()
		);
	}

	public static TrainerListResponse toListResponse(Trainer trainer, Map<Long, List<String>> awardsMap) {
		return new TrainerListResponse(
			trainer.getTrainerId(),
			trainer.getTrainerName(),
			trainer.getProfile(),
			trainer.getScore(),
			trainer.getIntro(),
			trainer.getCareer(),
			trainer.getField(),
			awardsMap.getOrDefault(trainer.getTrainerId(), Collections.emptyList()) // 수상 경력 없으면 빈 리스트
		);
	}

}
