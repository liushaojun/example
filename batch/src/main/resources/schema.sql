DROP TABLE person IF EXISTS;

CREATE TABLE person  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);