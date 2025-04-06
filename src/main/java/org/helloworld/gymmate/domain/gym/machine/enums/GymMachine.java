package org.helloworld.gymmate.domain.gym.machine.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public enum GymMachine {
	BENCH_PRESS("벤치 프레스",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/73e7696d-186a-4e35-991f-f478819b0709.jpg"),
	SQUAT_RACK("스쿼트 랙",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/1018ce3b-0787-429e-9e79-6acb8c3f19d0.jpg"),
	LAT_PULLDOWN("랫 풀다운",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/870dda50-fbc9-49fd-a9a8-49bf42cd2a27.jpg"),
	LEG_PRESS("레그 프레스",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/6e4e4557-8278-4508-a0c0-fbb80c62eb35.jpg"),
	CHEST_PRESS("체스트 프레스",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/6b0588c5-0de7-43b6-8bc0-5cfc3845db50.jpg"),
	PEC_DECK("펙덱 머신",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/5ff0d849-4465-4ef5-8ebc-e7c9db9ebc42.jpg"),
	SHOULDER_PRESS("숄더 프레스",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/88f06b71-f1b8-4270-91d6-53b7f9c33a26.jpg"),
	LEG_EXTENSION("레그 익스텐션",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/b8c76ab5-d076-4c14-94cb-2996029021cb.jpg"),
	LEG_CURL("레그 컬",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/9492d250-8619-4b2d-adaf-72c046f82000.jpg"),
	CABLE_MACHINE("케이블 머신",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/9162c463-16ad-4445-bb9e-2a39202a6916.jpg"),
	BARBELLS("바벨 세트",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/d4cc38e5-3c78-42b5-befe-62b3dc4e34db.jpg"),
	TREADMILL("러닝머신",
		"https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/machine/20250404/76e1ad7d-7306-4644-911f-579d72dff967.jpg");

	private final String name;
	private final String imageUrl;

	GymMachine(String name, String imageUrl) {
		this.name = name;
		this.imageUrl = imageUrl;
	}

	// 중복 없이 랜덤 n개 선택
	public static List<GymMachine> getRandomMachines(int count) {
		List<GymMachine> machines = new ArrayList<>(Arrays.asList(values()));
		Collections.shuffle(machines);
		return machines.subList(0, Math.min(count, machines.size()));
	}
}