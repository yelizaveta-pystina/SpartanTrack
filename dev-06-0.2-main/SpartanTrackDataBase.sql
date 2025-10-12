-- SpartanTrack Database
-- run this file to create database and table

CREATE DATABASE IF NOT EXISTS spartantrack;
USE spartantrack;

CREATE TABLE IF NOT EXISTS programming_languages (
                                                     language_id INT PRIMARY KEY AUTO_INCREMENT,
                                                     language_name VARCHAR(100) NOT NULL UNIQUE
);



-- to verify database creation...
SELECT 'Database setup complete!' AS status;

-- to show all the tables in the database
SHOW TABLES;

-- to display the structure of the programming_languages table
DESCRIBE programming_languages;

-- to display the current data in the table (table empty initially)
SELECT * FROM programming_languages;

ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;