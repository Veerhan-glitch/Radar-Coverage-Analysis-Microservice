CREATE TABLE IF NOT EXISTS users (
    name VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);
INSERT INTO users (name, password) VALUES
('admin', 'admin123'),
('user1', 'password1');
