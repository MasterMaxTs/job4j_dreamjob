CREATE TABLE post
(
    id   SERIAL PRIMARY KEY,
    name TEXT,
    city_id INTEGER UNIQUE,
    description TEXT,
    created TIMESTAMP
);