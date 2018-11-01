CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    version INT NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO users
    (version, username, password) VALUES
    (1, 'vartanbeno', 'hello123'),
    (1, 'johndoe', 'test123');
