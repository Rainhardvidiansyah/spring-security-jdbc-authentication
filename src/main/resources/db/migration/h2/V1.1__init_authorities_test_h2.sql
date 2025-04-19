CREATE TABLE authorities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    authorities_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP,
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP
);