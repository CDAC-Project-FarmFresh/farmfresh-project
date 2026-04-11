-- Phone verification fields on farm_user; password reset tokens

ALTER TABLE farm_user
    ADD COLUMN phone_verified BIT(1) NOT NULL DEFAULT 0 AFTER phone_no,
    ADD COLUMN phone_otp_hash VARCHAR(64) NULL AFTER phone_verified,
    ADD COLUMN phone_otp_expires_at DATETIME(6) NULL AFTER phone_otp_hash;

CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    consumed_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_prt_token_hash (token_hash),
    KEY idx_prt_user (user_id),
    CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES farm_user (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
