-- Create database
CREATE DATABASE IF NOT EXISTS employeeData;
USE employeeData;

-- EMPLOYEES TABLE
CREATE TABLE employees (
    empid INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    ssn CHAR(9) NOT NULL UNIQUE,
    dob DATE NOT NULL,
    hire_date DATE NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL,
    status ENUM('ACTIVE','TERMINATED') DEFAULT 'ACTIVE'
) ENGINE=InnoDB;

-- DIVISION TABLE
CREATE TABLE division (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- EMPLOYEE_DIVISION TABLE
CREATE TABLE employee_division (
    empid INT PRIMARY KEY,
    div_id INT NOT NULL,
    FOREIGN KEY (empid) REFERENCES employees(empid) ON DELETE CASCADE,
    FOREIGN KEY (div_id) REFERENCES division(id)
) ENGINE=InnoDB;

-- JOB TITLES TABLE
CREATE TABLE job_titles (
    job_title_id INT AUTO_INCREMENT PRIMARY KEY,
    title_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- EMPLOYEE_JOB_TITLES TABLE
CREATE TABLE employee_job_titles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empid INT NOT NULL,
    job_title_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE DEFAULT NULL,
    FOREIGN KEY (empid) REFERENCES employees(empid) ON DELETE CASCADE,
    FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id)
) ENGINE=InnoDB;

-- PAYROLL TABLE
CREATE TABLE payroll (
    pay_id INT AUTO_INCREMENT PRIMARY KEY,
    empid INT NOT NULL,
    pay_date DATE NOT NULL,
    gross_pay DECIMAL(10,2) NOT NULL,
    net_pay DECIMAL(10,2) NOT NULL,
    month_year CHAR(7) NOT NULL, -- e.g. '2025-12'
    FOREIGN KEY (empid) REFERENCES employees(empid) ON DELETE CASCADE
) ENGINE=InnoDB;

-- STATE TABLE
CREATE TABLE state (
    state_id INT AUTO_INCREMENT PRIMARY KEY,
    code CHAR(2) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- CITY TABLE
CREATE TABLE city (
    city_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- ADDRESS TABLE
CREATE TABLE address (
    empid INT PRIMARY KEY,
    street VARCHAR(200) NOT NULL,
    city_id INT NOT NULL,
    state_id INT NOT NULL,
    zip CHAR(10) NOT NULL,
    gender VARCHAR(20),
    identified_race VARCHAR(50),
    phone VARCHAR(20),
    FOREIGN KEY (empid) REFERENCES employees(empid) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES city(city_id),
    FOREIGN KEY (state_id) REFERENCES state(state_id)
) ENGINE=InnoDB;

-- USER LOGIN / ROLES
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    empid INT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empid) REFERENCES employees(empid)
) ENGINE=InnoDB;

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
) ENGINE=InnoDB;

-- AUDIT LOG
CREATE TABLE audit_log (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(50),
    object_type VARCHAR(50),
    object_id VARCHAR(100),
    ts DATETIME DEFAULT CURRENT_TIMESTAMP,
    detail TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- INDEXES
CREATE INDEX idx_emp_ssn ON employees(ssn);
CREATE INDEX idx_emp_lastname ON employees(last_name);
CREATE INDEX idx_payroll_month ON payroll(month_year);
CREATE INDEX idx_empdivision_div ON employee_division(div_id);
CREATE INDEX idx_empjob_job ON employee_job_titles(job_title_id);
