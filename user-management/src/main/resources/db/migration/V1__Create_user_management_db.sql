-- V1__Create_user_management_db.sql
CREATE DATABASE IF NOT EXISTS user_management_db;

USE user_management_db;

CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER', 'AUDITOR') DEFAULT 'USER'
    );