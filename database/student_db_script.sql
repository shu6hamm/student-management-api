-- =====================================================
-- Student Management API: MySQL script
-- Database: student_db
-- =====================================================

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- ---------- Tables ----------
-- (Hibernate creates these automatically with ddl-auto=update;
--  this script is for manual setup.)

CREATE TABLE IF NOT EXISTS users (
                                     id       BIGINT       NOT NULL AUTO_INCREMENT,
                                     username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,          -- BCrypt hash, never plain text
    role     ENUM('ADMIN','USER') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username),
    UNIQUE KEY uk_users_email (email)
    );

CREATE TABLE IF NOT EXISTS students (
                                        id     BIGINT       NOT NULL AUTO_INCREMENT,
                                        name   VARCHAR(255) NOT NULL,
    email  VARCHAR(255) NOT NULL,
    branch VARCHAR(255),
    year   INT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_students_email (email)
    );

-- ---------- Sample data ----------
INSERT INTO students (name, email, branch, year) VALUES
                                                     ('Aarav Patil',   'aarav@example.com',  'IT',   3),
                                                     ('Priya Desai',   'priya@example.com',  'CSE',  3),
                                                     ('Rohan Mehta',   'rohan@example.com',  'ENTC', 2),
                                                     ('Sneha Kulkarni','sneha@example.com',  'IT',   4);

-- Users are NOT inserted here on purpose: passwords must be BCrypt-hashed
-- by the application. Create a user with:
--   POST /api/auth/register  { "username": "...", "email": "...", "password": "..." }