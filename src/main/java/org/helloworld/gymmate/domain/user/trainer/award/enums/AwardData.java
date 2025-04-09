package org.helloworld.gymmate.domain.user.trainer.award.enums;

import lombok.Getter;

@Getter
public enum AwardData {
    AWARD_1("2025", "WBFF KOREA(국제 아마추어전) 1등", "남자 일반부 - 체급별 1위"),
    AWARD_2("2024", "서울특별시 보디빌딩 대회 1등", "남자 고등부 - 전체 1위"),
    AWARD_3("2023", "전국 피트니스 챔피언십 1등", "남자 피지크 - 체급별 1위"),
    AWARD_4("2025", "대한민국 클래식 보디빌딩 2등", "남자 마스터즈 - 체급별 2위"),
    AWARD_5("2022", "부산광역시 스포츠대회 3등", "남자 피지크 - 전체 3위"),
    AWARD_6("2023", "IFBB Korea 1등", "남자 피지크 - 체급별 1위"),
    AWARD_7("2021", "인천 전국 보디빌딩 대회 3등", "남자 일반부 - 체급별 3위"),
    AWARD_8("2025", "경기도 피트니스 페스티벌 1등", "남자 피지크 - 체급별 1위"),
    AWARD_9("2024", "WBFF ASIA(국제 프로 & 아마추어전) 2등", "남자 일반부 - 전체 2위"),
    AWARD_10("2023", "KBBF 보디빌딩 대회", "남자 일반부 - 전체 1위"),
    AWARD_11("2022", "대구광역시 보디빌딩 선수권 2등", "남자 피지크 - 체급별 2위"),
    AWARD_12("2021", "미스터제주 선발대회 1등", "남자 피지크 - 체급별 1위");

    private final String awardYear;
    private final String awardName;
    private final String awardInfo;

    AwardData(String awardYear, String awardName, String awardInfo) {
        this.awardYear = awardYear;
        this.awardName = awardName;
        this.awardInfo = awardInfo;
    }
}