-- PostgreSQL & H2 (PostgreSQL mode) compatible DDL

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE authorities (
    id BIGSERIAL PRIMARY KEY,
    authorities_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE user_authorities (
    user_id BIGINT NOT NULL,
    authorities_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, authorities_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (authorities_id) REFERENCES authorities(id)
);
