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
    name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- EMPLOYEE_DIVISION TABLE
CREATE TABLE employee_division (
    empid INT PRIMARY KEY,
    div_id INT NOT NULL,
    FOREIGN KEY (empid) REFERENCES employees(empid),
    FOREIGN KEY (div_id) REFERENCES division(id)
) ENGINE=InnoDB;

-- JOB TITLES TABLE
CREATE TABLE job_titles (
    job_title_id INT AUTO_INCREMENT PRIMARY KEY,
    title_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- EMPLOYEE_JOB_TITLES TABLE
CREATE TABLE employee_job_titles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empid INT NOT NULL,
    job_title_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE DEFAULT NULL,
    FOREIGN KEY (empid) REFERENCES employees(empid),
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
    FOREIGN KEY (empid) REFERENCES employees(empid)
) ENGINE=InnoDB;

-- STATE TABLE
CREATE TABLE state (
    state_id INT PRIMARY KEY,
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
    dob DATE,
    mobile_phone VARCHAR(20),
    FOREIGN KEY (empid) REFERENCES employees(empid),
    FOREIGN KEY (city_id) REFERENCES city(city_id),
    FOREIGN KEY (state_id) REFERENCES state(state_id)
) ENGINE=InnoDB;

-- INDEXES
CREATE INDEX idx_emp_ssn ON employees(ssn);
CREATE INDEX idx_emp_lastname ON employees(last_name);
CREATE INDEX idx_payroll_month ON payroll(month_year);
CREATE INDEX idx_empdivision_div ON employee_division(div_id);
CREATE INDEX idx_empjob_job ON employee_job_titles(job_title_id);
