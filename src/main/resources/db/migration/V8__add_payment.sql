-- 22. payment
CREATE TABLE payment (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    toss_order_id VARCHAR(100) NOT NULL,
    payment_key VARCHAR(50) NOT NULL,
    paid_amount BIGINT NOT NULL,
    success_date DATETIME,
    method VARCHAR(50) NOT NULL
);