-- 23. trainer_review
CREATE TABLE trainer_review (
    trainer_review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    score DOUBLE NOT NULL,
    content VARCHAR(500),
    member_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    created_at DATETIME,
    modified_at DATETIME,
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
);

-- 24. trainer_review_image
CREATE TABLE trainer_review_image (
    trainer_review_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    trainer_review_id BIGINT,
    FOREIGN KEY (trainer_review_id) REFERENCES trainer_review(trainer_review_id)
);