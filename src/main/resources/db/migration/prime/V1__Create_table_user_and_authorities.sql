CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    isEnabled BOOLEAN NOT NULL
);


CREATE TABLE authorities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    authorities_name VARCHAR(255) NOT NULL
);

CREATE TABLE user_authorities (
    user_id BIGINT,
    authorities_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (authorities_id) REFERENCES authorities(id),
    PRIMARY KEY (user_id, authorities_id)
);
