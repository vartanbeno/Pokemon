CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL,
    version INT NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO users
    (id, version, username, password) VALUES
    (1, 1, 'vartanbeno', 'hello123'),
    (2, 1, 'johndoe', 'test123');
