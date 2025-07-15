CREATE TABLE _user
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    email    VARCHAR(255)                        NOT NULL UNIQUE,
    username VARCHAR(255)                        NOT NULL UNIQUE,
    password VARCHAR(255)                        NOT NULL,
    CONSTRAINT pk__user PRIMARY KEY (id)
);

CREATE TABLE refresh_token
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    token      VARCHAR(255)                        NOT NULL UNIQUE,
    expires_at TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    user_id    BIGINT                              NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id)
);

CREATE TABLE role
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name      VARCHAR(255)                        NOT NULL,
    role_name VARCHAR(255)                        NOT NULL UNIQUE,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (role_id, user_id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESH_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES _user (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES _user (id);
