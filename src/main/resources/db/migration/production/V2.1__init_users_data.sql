INSERT INTO users (email, password, username, enabled) VALUES
('rainhard@email.com', '$2a$12$6u8Mp34rZLl7oONcEgeAHOBX1kdU7dCyXb7vPgR.6IvuWYV1u0iu.', 'rainhard', true),
('maulida@email.com', '$2a$12$6u8Mp34rZLl7oONcEgeAHOBX1kdU7dCyXb7vPgR.6IvuWYV1u0iu.', 'maulida', true),
('bintang@example.com', '$2a$12$6u8Mp34rZLl7oONcEgeAHOBX1kdU7dCyXb7vPgR.6IvuWYV1u0iu.', 'bintang', false),
('ratna@example.com', '$2a$12$6u8Mp34rZLl7oONcEgeAHOBX1kdU7dCyXb7vPgR.6IvuWYV1u0iu.', 'ratna', false);


INSERT INTO authorities (authorities_name) VALUES
('ROLE_SUPER_ADMIN'),
('ROLE_ADMIN'),
('ROLE_MODERATOR'),
('ROLE_USER');