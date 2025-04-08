-- 19. Student
CREATE TABLE student (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25),
    progress VARCHAR(255),
    memo VARCHAR(500),
    profile_url VARCHAR(255),
    member_id BIGINT,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id)
);