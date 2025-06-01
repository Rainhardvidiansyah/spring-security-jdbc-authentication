CREATE TABLE user_authorities (
    user_id BIGINT,
    authorities_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (authorities_id) REFERENCES authorities(id),
    PRIMARY KEY (user_id, authorities_id)
   )