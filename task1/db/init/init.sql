CREATE TABLE users (
  name VARCHAR(50) PRIMARY KEY,
  password VARCHAR(100) NOT NULL
);

INSERT INTO users (name, password)
VALUES ('admin','admin123');
