CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80),
    email       VARCHAR(250),
    password    VARCHAR(30),
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS post
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP,
    city_id     INTEGER,
    visible     BOOLEAN
);

CREATE TABLE IF NOT EXISTS candidate
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80),
    description TEXT,
    created     TIMESTAMP,
    photo       BYTEA
);