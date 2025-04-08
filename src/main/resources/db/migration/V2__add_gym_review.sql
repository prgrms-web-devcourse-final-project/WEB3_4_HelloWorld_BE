-- 20. gym_review
CREATE TABLE gym_review (
    gym_review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    score DOUBLE NOT NULL,
    content VARCHAR(500),
    member_id BIGINT NOT NULL,
    partner_gym_id BIGINT NOT NULL,
    created_at DATETIME,
    modified_at DATETIME
);

-- 21. gym_review_image
CREATE TABLE gym_review_image (
    gym_review_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    gym_review_id BIGINT,
    FOREIGN KEY (gym_review_id) REFERENCES gym_review(gym_review_id)
);