CREATE TABLE Class(class_id SERiAL PRIMARY KEY UNIQUE, course_number VARCHAR(20) NOT NULL,
 term VARCHAR(20) NOT NULL, section_number VARCHAR(20) NOT NULL, description TEXT);

CREATE TABLE Student(username VARCHAR(100) NOT NULL UNIQUE, student_id VARCHAR(9) PRIMARY KEY UNIQUE,
name VARCHAR(100) NOT NULL, class_id INTEGER REFERENCES class(class_id));

CREATE TABLE Items(id SERIAL PRIMARY KEY UNIQUE, itemname VARCHAR(100) NOT NULL UNIQUE, category_name VARCHAR(50) REFERENCES Categories(category_name),
description TEXT NOT NULL, point_value INTEGER NOT NULL);

CREATE TABLE Categories(id SERIAL PRIMARY KEY UNIQUE, category_name VARCHAR(50) NOT NULL UNIQUE, weight VARCHAR(5) NOT NULL);

CREATE TABLE Student_Graded_Items(itemname VARCHAR(50) REFERENCES items(itemname),
username VARCHAR(50) REFERENCES Student(username), points INTEGER, PRIMARY KEY(itemname, username));
