-- Удаляем текущие подключения к базе users_db и удаляем базу
-- DO $$
--     BEGIN
--         PERFORM pg_terminate_backend(pg_stat_activity.pid)
--         FROM pg_stat_activity
--         WHERE pg_stat_activity.datname = 'users_db' AND pid <> pg_backend_pid();
--     EXCEPTION
--         WHEN others THEN
--             RAISE NOTICE 'No active connections to terminate';
--     END $$;
--
-- -- Удаляем базу данных
-- DROP DATABASE IF EXISTS users_db;
--
-- -- Создаём новую базу данных
-- CREATE DATABASE users_db WITH TEMPLATE = template0 ENCODING = 'UTF8';
--
-- -- Назначаем владельца базы
-- ALTER DATABASE users_db OWNER TO "user";

-- Создаём таблицы
CREATE TABLE public.confirmations (
                                      id BIGINT NOT NULL,
                                      created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                      created_by BIGINT NOT NULL,
                                      reference_id VARCHAR(255),
                                      updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                      updated_by BIGINT NOT NULL,
                                      key VARCHAR(255),
                                      user_id BIGINT NOT NULL
);
ALTER TABLE public.confirmations OWNER TO "user";

CREATE TABLE public.credentials (
                                    id BIGINT NOT NULL,
                                    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                    created_by BIGINT NOT NULL,
                                    reference_id VARCHAR(255),
                                    updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                                    updated_by BIGINT NOT NULL,
                                    password VARCHAR(255),
                                    user_id BIGINT NOT NULL
);
ALTER TABLE public.credentials OWNER TO "user";

CREATE TABLE public.users (
                              id BIGINT NOT NULL,
                              user_id VARCHAR(255) NOT NULL,
                              created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                              created_by BIGINT NOT NULL,
                              reference_id VARCHAR(255),
                              updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
                              updated_by BIGINT NOT NULL,
                              email VARCHAR(255) NOT NULL,
                              first_name VARCHAR(255),
                              last_name VARCHAR(255),
                              login_attempts INT,
                              phone VARCHAR(255),
                              bio VARCHAR(255),
                              image_url VARCHAR(255),
                              enabled BOOLEAN NOT NULL DEFAULT TRUE,
                              account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
                              account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
                              qr_code_image_url TEXT,
                              qr_code_secret VARCHAR(255)
);
ALTER TABLE public.users OWNER TO "user";

CREATE TABLE public.roles (
                              id BIGINT NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              authorities TEXT
);
ALTER TABLE public.roles OWNER TO "user";

CREATE TABLE public.user_roles (
                                   user_id BIGINT,
                                   role_id BIGINT
);
ALTER TABLE public.user_roles OWNER TO "user";

-- Создаём последовательности
CREATE SEQUENCE public.primary_key_seq START 1 INCREMENT 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.primary_key_seq OWNER TO "user";

-- Вставка данных
INSERT INTO public.roles VALUES (1, 'ADMIN', 'user:create, user:read, user:update, document:create, document:read, document:update, document:delete');
INSERT INTO public.roles VALUES (2, 'USER', 'document:create, document:read, document:update');


-- INSERT INTO public.users
-- (id, user_id, created_at, created_by, reference_id, updated_at, updated_by, email, first_name, last_name, login_attempts, phone, bio, image_url, enabled, account_non_expired, account_non_locked, qr_code_image_url, qr_code_secret)
-- VALUES
--     (0, '123e4567-e89b-12d3-a456-426614174000', '2024-01-29 22:10:47.925942', 0, 'system', '2024-01-29 22:10:47.926642', 0, 'system@gmail.com', 'System', 'System', 0, '1234567890', 'This is not a user but the system itself', 'https://cdn-icons-png.flaticon.com/128/2911/2911833.png', true, true, true, NULL, NULL);



-- INSERT INTO public.users VALUES
--     (1, '2024-01-29 22:10:47.925942', 0, 'system', '2024-01-29 22:10:47.926642', 0, 'system@gmail.com', 'System', 'System', 0, '1234567890', 'This is not a user but the system itself', 'https://cdn-icons-png.flaticon.com/128/2911/2911833.png', true, true, true, NULL, NULL);

-- Добавляем ограничения
ALTER TABLE ONLY public.users ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.users ADD CONSTRAINT "UK6j5t70rd2eub907qysjvvd76n" UNIQUE (email);
ALTER TABLE ONLY public.users ADD CONSTRAINT "UK8lihxghut7f8wccc8etpyi7yl" UNIQUE (user_id);

ALTER TABLE ONLY public.roles ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_roles ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);
ALTER TABLE public.user_roles ADD CONSTRAINT "FK7ov27fyo7ebsvada1ej7qkphl" FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE public.user_roles ADD CONSTRAINT "FKej3jidxlte0r8flpavhiso3g6" FOREIGN KEY (role_id) REFERENCES public.roles(id);
