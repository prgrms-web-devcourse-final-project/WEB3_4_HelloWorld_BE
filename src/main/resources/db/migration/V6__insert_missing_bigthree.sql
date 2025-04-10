-- gymmate_member 테이블의 모든 회원 중에서,
-- 해당 회원의 member_id로 bigthree 레코드가 하나도 존재하지 않는 경우
-- 오늘 날짜로 bigthree 생성
INSERT INTO bigthree (bench, deadlift, squat, `date`, member_id)
SELECT m.recent_bench,
       m.recent_deadlift,
       m.recent_squat,
       CURRENT_DATE,
       m.member_id
FROM gymmate_member m
WHERE NOT EXISTS (SELECT 1
                  FROM bigthree b
                  WHERE b.member_id = m.member_id);
