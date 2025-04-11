package org.helloworld.gymmate.dummy.trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public enum PtProductDummy {

    PT_PODUCT_1("근육 증가 벌크업 PT", "웨이트 위주의 고강도 트레이닝, 체중 증가 & 근육량 증진 목표", 30000L),
    PT_PODUCT_2("다이어트 PT", "체지방 감량에 집중된 유산소+근력 복합 트레이닝, 식단 가이드 포함", 40000L),
    PT_PODUCT_3("1:1 맞춤 재활 PT", "부상 이후 회복을 위한 저강도 안정성 운동 + 자세 교정 중심", 50000L),
    PT_PODUCT_4("하체 강화 & 골반 교정 PT", "약한 하체 근육 강화 + 골반 틀어짐 개선 스트레칭 포함", 60000L),
    PT_PODUCT_5("홈트 입문자를 위한 PT 기초", "운동 기구 없이 체중 활용, 올바른 자세 습득 중심", 50000L),
    PT_PODUCT_6("체형 교정 & 자세 밸런스", "라운드숄더, 거북목, 틀어진 골반 개선용 스트레칭 + 근육 강화", 70000L),
    PT_PODUCT_7("시니어 건강관리 PT", "50대 이상을 위한 관절 보호 운동 + 균형감 유지 훈련", 80000L);

    private final String ptProductName;
    private final String ptProductInfo;
    private final Long ptProductFee;

    PtProductDummy(String ptProductName, String ptProductInfo, Long ptProductFee) {
        this.ptProductName = ptProductName;
        this.ptProductInfo = ptProductInfo;
        this.ptProductFee = ptProductFee;
    }

    public static PtProductDummy getRandomPtProductDummy() {
        List<PtProductDummy> allPtProductDummy = new ArrayList<>(Arrays.asList(values()));
        Collections.shuffle(allPtProductDummy);
        return allPtProductDummy.get(0);
    }
}
