-- 1. 기존 AUTO_INCREMENT 제거
ALTER TABLE bigthree_average
    MODIFY bigthree_average_id BIGINT NOT NULL;

-- 2. ID가 항상 1인지 확인하는 CHECK 제약 추가
ALTER TABLE bigthree_average
    ADD CONSTRAINT chk_bigthree_average_id CHECK (bigthree_average_id = 1);