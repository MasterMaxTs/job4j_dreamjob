CREATE TABLE candidate
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80),
    description TEXT,
    created     TIMESTAMP,
    photo       BYTEA
);