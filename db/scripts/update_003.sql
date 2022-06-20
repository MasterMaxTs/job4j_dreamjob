CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(80),
    email       VARCHAR(250),
    password    VARCHAR(30),
    CONSTRAINT email_unique UNIQUE (email)
);