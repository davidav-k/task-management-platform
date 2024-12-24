
-- -- Table: auditable
-- -- This is a parent table to store common audit fields
-- CREATE SEQUENCE IF NOT EXISTS primary_key_seq;
--
-- -- Table: users
-- CREATE TABLE IF NOT EXISTS users (
--     id BIGINT PRIMARY KEY DEFAULT nextval('primary_key_seq'),
--     reference_id VARCHAR(255) DEFAULT gen_random_uuid(),
--     created_by BIGINT NOT NULL,
--     updated_by BIGINT NOT NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     user_id VARCHAR(255) NOT NULL UNIQUE,
--     first_name VARCHAR(255),
--     last_name VARCHAR(255),
--     email VARCHAR(255) NOT NULL UNIQUE,
--     login_attempts INTEGER,
--     last_login DATE,
--     phone VARCHAR(20),
--     bio TEXT,
--     image_url VARCHAR(2083),
--     account_non_expired BOOLEAN DEFAULT TRUE,
--     account_non_locked BOOLEAN DEFAULT TRUE,
--     enabled BOOLEAN DEFAULT TRUE,
--     mfa BOOLEAN DEFAULT FALSE,
--     qr_code_secret VARCHAR(255),
--     qr_code_image_url TEXT
--     );
--
-- -- Table: roles
-- CREATE TABLE IF NOT EXISTS roles (
--     id BIGINT PRIMARY KEY DEFAULT nextval('primary_key_seq'),
--     reference_id VARCHAR(255) DEFAULT gen_random_uuid(),
--     created_by BIGINT NOT NULL,
--     updated_by BIGINT NOT NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     name VARCHAR(255),
--     authorities TEXT
--     );
--
-- -- Table: user_roles
-- CREATE TABLE IF NOT EXISTS user_roles (
--     user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
--     role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
--     PRIMARY KEY (user_id, role_id)
--     );
--
-- -- Table: confirmations
-- CREATE TABLE IF NOT EXISTS confirmations (
--     id BIGINT PRIMARY KEY DEFAULT nextval('primary_key_seq'),
--     reference_id VARCHAR(255) DEFAULT gen_random_uuid(),
--     created_by BIGINT NOT NULL,
--     updated_by BIGINT NOT NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     key VARCHAR(255),
--     user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
--     );
--
-- -- Table: credentials
-- CREATE TABLE IF NOT EXISTS credentials (
--     id BIGINT PRIMARY KEY DEFAULT nextval('primary_key_seq'),
--     reference_id VARCHAR(255) DEFAULT gen_random_uuid(),
--     created_by BIGINT NOT NULL,
--     updated_by BIGINT NOT NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     password VARCHAR(255),
--     user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
--     );
--
-- -- Indexes for quick lookups
-- CREATE INDEX IF NOT EXISTS idx_users_user_id ON users(user_id);
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(name);
-- CREATE INDEX IF NOT EXISTS idx_confirmations_key ON confirmations(key);
-- CREATE INDEX IF NOT EXISTS idx_credentials_user_id ON credentials(user_id);
--
-- -- Triggers to update timestamps
-- CREATE OR REPLACE FUNCTION update_timestamp() RETURNS TRIGGER AS $$
-- BEGIN
--     NEW.updated_at = CURRENT_TIMESTAMP;
-- RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- -- Apply triggers to track updates
-- CREATE TRIGGER update_users_timestamp
--     BEFORE UPDATE ON users
--     FOR EACH ROW
--     EXECUTE FUNCTION update_timestamp();
--
-- CREATE TRIGGER update_roles_timestamp
--     BEFORE UPDATE ON roles
--     FOR EACH ROW
--     EXECUTE FUNCTION update_timestamp();
--
-- CREATE TRIGGER update_confirmations_timestamp
--     BEFORE UPDATE ON confirmations
--     FOR EACH ROW
--     EXECUTE FUNCTION update_timestamp();
--
-- CREATE TRIGGER update_credentials_timestamp
--     BEFORE UPDATE ON credentials
--     FOR EACH ROW
--     EXECUTE FUNCTION update_timestamp();

INSERT INTO public.users
(id, user_id, created_at, created_by, reference_id,
 updated_at, updated_by, email, first_name, last_name,
 login_attempts, phone, bio, image_url,
 enabled, mfa, account_non_expired, account_non_locked, qr_code_image_url, qr_code_secret)
VALUES
    (0, '123e4567-e89b-12d3-a456-426614174000', '2024-01-29 22:10:47.925942', 0, 'system',
     '2024-01-29 22:10:47.926642', 0, 'system@gmail.com', 'System', 'System',
     0, '1234567890', 'This is not a user but the system itself', 'https://cdn-icons-png.flaticon.com/128/2911/2911833.png',
     true,true, true, true, NULL, NULL);
