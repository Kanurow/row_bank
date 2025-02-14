create schema rowbank;

CREATE TABLE IF NOT EXISTS users_table (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(25) NOT NULL CHECK (LENGTH(first_name) >= 2),
    last_name VARCHAR(25) NOT NULL CHECK (LENGTH(last_name) >= 2),
    username VARCHAR(20) NOT NULL UNIQUE CHECK (LENGTH(username) >= 2),
    account_number VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE CHECK (date_of_birth <= CURRENT_DATE),
    email VARCHAR(255) NOT NULL UNIQUE CHECK (email LIKE '%@%'),
    bank_name VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 2) CHECK (balance >= 0.0),
    password VARCHAR(255) NOT NULL,
    account_locked BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
