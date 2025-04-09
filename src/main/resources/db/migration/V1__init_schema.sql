-- 1. Oauth
CREATE TABLE oauth (
    oauth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- provider_type ENUM('KAKAO', 'GOOGLE', 'NAVER', 'APPLE') NOT NULL,
    provider_type VARCHAR(10) NOT NULL,
    provider_id VARCHAR(255) NOT NULL, -- 몇 글자로 오는 지 모름
    -- user_type  ENUM('MEMBER', 'TRAINER') NOT NULL, -- @Convert(converter = UserTypeConverter.class)
    user_type VARCHAR(10) NOT NULL,
    created_at DATETIME, -- BaseEntity 적용 시 자동 추가됨
    modified_at DATETIME -- 사용하지 않지만 BaseEntity 적용 시 자동 추가됨
);

-- 2. Member
CREATE TABLE gymmate_member ( -- 소셜 로그인 시  null 상태로 데이터 생성, 이후 회원가입 페이지에서 데이터 받음
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(50) UNIQUE,
    member_name VARCHAR(25),
    email VARCHAR(50),
    birthday VARCHAR(50),
    -- gender_type ENUM('MALE', 'FEMALE'), -- @Convert(converter = GenderTypeConverter.class)
    gender_type VARCHAR(10),
    height VARCHAR(25),
    weight VARCHAR(25),
    address VARCHAR(50),
    x_field DOUBLE,
    y_field DOUBLE,
    recent_bench DOUBLE,
    recent_deadlift DOUBLE,
    recent_squat DOUBLE,
    `level` INT, /* 예약어 */
    is_account_nonlocked BOOLEAN,
    additional_info_completed BOOLEAN,
    profile_url VARCHAR(255), -- url 길이 일반적으로 varchar(255) 권장
    cash BIGINT,
    oauth_id BIGINT UNIQUE,
    FOREIGN KEY (oauth_id) REFERENCES oauth(oauth_id)
);

-- 3. Facility
CREATE TABLE facility (
    facility_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parking BOOLEAN NOT NULL,
    shower_room BOOLEAN NOT NULL,
    in_body BOOLEAN NOT NULL,
    locker BOOLEAN NOT NULL,
    wifi BOOLEAN NOT NULL,
    sports_wear BOOLEAN NOT NULL,
    towel BOOLEAN NOT NULL,
    sauna BOOLEAN NOT NULL
);

-- 4. Gym
CREATE TABLE gym (
    gym_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gym_name VARCHAR(50),
    start_time VARCHAR(20),
    end_time VARCHAR(20),
    phone_number VARCHAR(50),
    is_partner BOOLEAN,
    address VARCHAR(50),
    location POINT SRID 4326 NOT NULL, -- 공간 인덱스 사용을 위해 not null 필수
    avg_score DOUBLE,
    intro VARCHAR(255),
    place_url VARCHAR(255),
    facility_id BIGINT UNIQUE,
    -- 이미지 리스트는 칼럼으로 추가하지 않음, 5번째에 생성함
    FOREIGN KEY (facility_id) REFERENCES facility(facility_id)
);

-- POINT 칼럼 사용 설정
CREATE SPATIAL INDEX idx_gym_location ON gym(location);

-- 5. GymImage
CREATE TABLE gym_image (
    gym_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255),
    gym_id BIGINT,
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id)
);

-- 6. Trainer
CREATE TABLE gymmate_trainer (
    trainer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_name VARCHAR(25),
    phone_number VARCHAR(50) UNIQUE,
    email VARCHAR(50),
    -- gender_type ENUM('MALE', 'FEMALE'), -- @Convert(converter = GenderTypeConverter.class)
    gender_type VARCHAR(10),
    bank VARCHAR(25),
    account VARCHAR(50),
    business_number VARCHAR(50) UNIQUE,
    business_date DATE,
    is_owner BOOLEAN,
    is_check BOOLEAN,
    profile_url VARCHAR(255),
    is_account_non_locked BOOLEAN,
    cash BIGINT,
    score DOUBLE,
    intro VARCHAR(100),
    career VARCHAR(50),
    field VARCHAR(50),
    additional_info_completed BOOLEAN,
    gym_id BIGINT,
    oauth_id BIGINT UNIQUE,
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id),
    FOREIGN KEY (oauth_id) REFERENCES oauth(oauth_id)
);

-- 7. Award
CREATE TABLE award (
    award_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    award_year VARCHAR(20) NOT NULL,
    award_name VARCHAR(50) NOT NULL,
    award_info VARCHAR(100) NOT NULL,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
);

-- 8. Classtime
CREATE TABLE class_time (
    class_time_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `time` INT NOT NULL, /* 예약어 */
    day_of_week INT NOT NULL,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
);

-- 9. PtProduct
CREATE TABLE pt_product (
    pt_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pt_product_name VARCHAR(50) NOT NULL,
    info VARCHAR(200) NOT NULL,
    pt_product_fee BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
    -- 이미지 리스트는 칼럼으로 추가하지 않음, 9번째에 생성함
);

-- 10. PtProductImage
CREATE TABLE pt_product_image (
    pt_product_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    pt_product_id BIGINT NOT NULL,
    FOREIGN KEY (pt_product_id) REFERENCES pt_product(pt_product_id)
);

-- 11. Reservation
CREATE TABLE reservation (
    reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    `date` DATE NOT NULL, /* 예약어 */
    `time` INT NOT NULL, /* 예약어 */
    price BIGINT NOT NULL,
    cancel_date DATE,
    completed_date DATE,
    member_id BIGINT,
    trainer_id BIGINT,
    FOREIGN KEY (member_id) REFERENCES gymmate_member(member_id),
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
);

-- 12. Machine
CREATE TABLE machine (
    machine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    machine_name VARCHAR(25) NOT NULL,
    amount INT NOT NULL,
    machine_image VARCHAR(255),
    gym_id BIGINT,
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id)
);

-- 13. PartnerGym
CREATE TABLE partner_gym (
    partner_gym_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL UNIQUE,
    gym_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id)
    -- gymProducts 리스트
);

-- 14. GymProduct
CREATE TABLE gym_product (
    gym_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gym_product_name VARCHAR(50) NOT NULL,
    gym_product_fee INT NOT NULL,
    gym_product_month INT NOT NULL,
    on_sale BOOLEAN NOT NULL,
    partner_gym_id BIGINT,
    FOREIGN KEY (partner_gym_id) REFERENCES partner_gym(partner_gym_id)
);

-- 15. GymTicket
CREATE TABLE gym_ticket (
    gym_ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gym_product_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    gym_product_fee INT NOT NULL,
    -- status ENUM('ACTIVE', 'EXPIRED', 'CANCELED') NOT NULL, -- @Convert(converter = GymTicketStatusConverter.class)
    status VARCHAR(10),
    member_id BIGINT NOT NULL,
    partner_gym_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES gymmate_member(member_id)
);

-- 16. Bigthree
CREATE TABLE bigthree (
    bigthree_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bench DOUBLE NOT NULL,
    deadlift DOUBLE NOT NULL,
    squat DOUBLE NOT NULL,
    `date` DATE NOT NULL, /* 예약어 */
    member_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES gymmate_member(member_id)
);

-- 17. Diary
CREATE TABLE diary (
    diary_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    `date` DATE NOT NULL, /* 예약어 */
    member_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES gymmate_member(member_id)
);

-- 18. BigthreeAverage
CREATE TABLE bigthree_average (
    bigthree_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sum_average DOUBLE NOT NULL,
    bench_average DOUBLE NOT NULL,
    deadlift_average DOUBLE NOT NULL,
    squat_average DOUBLE NOT NULL
);

-- 19. Student
CREATE TABLE student (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25),
    progress VARCHAR(255),
    memo VARCHAR(500),
    profile_url VARCHAR(255),
    member_id BIGINT,
    trainer_id BIGINT NOT NULL,
    FOREIGN KEY (trainer_id) REFERENCES gymmate_trainer(trainer_id)
);