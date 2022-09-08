CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(20) NOT NULL,
    email       VARCHAR(30) NOT NULL,
    password    VARCHAR(10) NOT NULL,
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS posts
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    created     TIMESTAMP,
    city_id     INTEGER,
    visible     BOOLEAN
);

CREATE TABLE IF NOT EXISTS candidates
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    created     TIMESTAMP,
    photo       BYTEA
);