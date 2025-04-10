-- 1. 대상 member_id를 임시 테이블에 저장 - recent 3대 무게 필드 중 하나라도 NULL인 경우
CREATE TEMPORARY TABLE temp_reset_members AS
SELECT member_id
FROM gymmate_member
WHERE recent_bench IS NULL
   OR recent_deadlift IS NULL
   OR recent_squat IS NULL;

-- 2. 임시 테이블 대상 중 NULL인 recent 무게 필드만 0으로 업데이트
UPDATE gymmate_member
SET recent_bench    = COALESCE(recent_bench, 0),
    recent_deadlift = COALESCE(recent_deadlift, 0),
    recent_squat    = COALESCE(recent_squat, 0)
WHERE member_id IN (SELECT member_id FROM temp_reset_members);

-- 3. 모든 회원의 level 재계산 (recent 값 기준 합산으로 계산)
UPDATE gymmate_member
SET `level` = CASE
                  WHEN (recent_bench + recent_deadlift + recent_squat) < 100 THEN 0
                  WHEN (recent_bench + recent_deadlift + recent_squat) < 200 THEN 1
                  WHEN (recent_bench + recent_deadlift + recent_squat) < 300 THEN 2
                  WHEN (recent_bench + recent_deadlift + recent_squat) < 400 THEN 3
                  ELSE 4
    END;

-- 4. 임시 테이블 대상 중 recent 값 기준으로 오늘 날짜의 bigthree 기록 데이터 삽입
INSERT INTO bigthree (bench, deadlift, squat, `date`, member_id)
SELECT recent_bench, recent_deadlift, recent_squat, CURRENT_DATE, member_id
FROM gymmate_member
WHERE member_id IN (SELECT member_id FROM temp_reset_members);

-- 5. 임시 테이블 삭제
DROP TEMPORARY TABLE temp_reset_members;
