-- 1. diary 테이블에 member_id와 date 조합 유니크 제약 추가
ALTER TABLE diary
    ADD CONSTRAINT uq_member_date UNIQUE (member_id, `date`);

-- 2. bigthree 테이블에 member_id와 date 조합 유니크 제약 추가
ALTER TABLE bigthree
    ADD CONSTRAINT uq_bigthree_date UNIQUE (member_id, `date`);
