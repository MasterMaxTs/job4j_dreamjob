CREATE TABLE post
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP,
    city_id     INTEGER UNIQUE
);