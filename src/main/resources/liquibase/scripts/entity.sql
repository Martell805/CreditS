-- liquibase formatted sql

-- changeSet martell:1
CREATE TABLE orders (
    id                  BIGSERIAL NOT NULL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    tariff_id           BIGINT NOT NULL,
    credit_rating       FLOAT NOT NULL,
    status              TEXT NOT NULL,
    time_insert         TEXT NOT NULL,
    time_update         TEXT NOT NULL
);

CREATE TABLE tariffs (
    id                  BIGSERIAL NOT NULL PRIMARY KEY,
    type                TEXT NOT NULL UNIQUE,
    interest_rate       TEXT NOT NULL
);

CREATE TABLE users (
    id                  BIGSERIAL NOT NULL PRIMARY KEY,
    username            TEXT NOT NULL,
    password            TEXT NOT NULL,
    email               TEXT NOT NULL,
    name                TEXT NOT NULL,
    passport            TEXT NOT NULL,
    role                TEXT NOT NULL,
    email_subscription  BOOLEAN NOT NULL
);
