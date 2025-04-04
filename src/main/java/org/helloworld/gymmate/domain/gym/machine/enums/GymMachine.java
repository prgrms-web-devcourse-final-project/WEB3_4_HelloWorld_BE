package org.helloworld.gymmate.domain.gym.machine.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public enum GymMachine {
	BENCH_PRESS("벤치 프레스", "s3://your-bucket/bench_press.jpg"),
	SQUAT_RACK("스쿼트 랙", "s3://your-bucket/squat_rack.jpg"),
	LAT_PULLDOWN("랫 풀다운", "s3://your-bucket/lat_pulldown.jpg"),
	LEG_PRESS("레그 프레스", "s3://your-bucket/leg_press.jpg"),
	CHEST_PRESS("체스트 프레스", "s3://your-bucket/chest_press.jpg"),
	PEC_DECK("펙덱 머신", "s3://your-bucket/pec_deck.jpg"),
	SHOULDER_PRESS("숄더 프레스", "s3://your-bucket/shoulder_press.jpg"),
	LEG_EXTENSION("레그 익스텐션", "s3://your-bucket/leg_extension.jpg"),
	LEG_CURL("레그 컬", "s3://your-bucket/leg_curl.jpg"),
	CABLE_MACHINE("케이블 머신", "s3://your-bucket/cable_machine.jpg"),
	DUMBBELLS("덤벨 세트", "s3://your-bucket/dumbbells.jpg"),
	BARBELLS("바벨 세트", "s3://your-bucket/barbells.jpg"),
	TREADMILL("러닝머신", "s3://your-bucket/treadmill.jpg");

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