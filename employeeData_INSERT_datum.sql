USE employeeData;
SET FOREIGN_KEY_CHECKS=0;

-- STATES
INSERT INTO state (state_id, code, name) VALUES
(1, 'GA', 'Georgia'),
(2, 'NY', 'New York'),
(3, 'CA', 'California');

-- CITIES
INSERT INTO city (city_id, name) VALUES
(1, 'Atlanta'),
(2, 'Savannah'),
(3, 'New York'),
(4, 'San Francisco');

-- DIVISIONS
INSERT INTO division (id, name) VALUES
(1, 'Engineering'),
(2, 'Marketing'),
(3, 'Human Resources'),
(4, 'Finance'),
(999, 'Headquarters');

-- JOB TITLES
INSERT INTO job_titles (job_title_id, title_name) VALUES
(100, 'Software Engineer'),
(101, 'Software Architect'),
(102, 'Software Developer'),
(200, 'Marketing Manager'),
(201, 'Marketing Associate'),
(900, 'Chief Executive Officer');

-- EMPLOYEES
INSERT INTO employees (empid, first_name, last_name, ssn, dob, hire_date, base_salary, status) VALUES
(1, 'Snoopy', 'Beagle', '111111111', '1990-01-01', '2022-08-01', 45000.00, 'ACTIVE'),
(2, 'Charlie', 'Brown', '111222222', '1989-05-10', '2022-07-01', 48000.00, 'ACTIVE'),
(3, 'Lucy', 'Doctor', '111333333', '1991-03-21', '2022-07-03', 55000.00, 'ACTIVE'),
(4, 'Peppermint', 'Patti', '111444444', '1988-11-11', '2022-08-02', 98000.00, 'ACTIVE'),
(5, 'Linus', 'Blanket', '111555555', '1992-02-02', '2022-09-01', 43000.00, 'ACTIVE'),
(6, 'Bugs', 'Bunny', '222111111', '1980-07-01', '2020-01-01', 76000.00, 'ACTIVE'),
(7, 'Daphne', 'Blake', '333111111', '1993-04-12', '2023-01-15', 60000.00, 'ACTIVE'),
(8, 'Velma', 'Dinkley', '333222222', '1987-09-01', '2021-06-01', 82000.00, 'ACTIVE'),
(9, 'Scooby', 'Doo', '444111111', '1994-02-22', '2024-03-01', 52000.00, 'ACTIVE'),
(10,'Shaggy','Rodgers','444222222','1994-02-22','2024-03-01',77000.00,'ACTIVE');

-- ADDRESS
INSERT INTO address (empid, street, city_id, state_id, zip, gender, identified_race, phone) VALUES
(1, '123 Main St', 1, 1, '30303', 'Male', 'Hispanic', '555-0101'),
(2, '456 Oak Ave', 1, 1, '30304', 'Male', 'White', '555-0202'),
(3, '789 Pine Rd', 1, 1, '30305', 'Female', 'Black', '555-0303'),
(4, '101 Maple Ln', 3, 2, '10019', 'Female', 'White', '555-0404'),
(5, '202 Birch Blvd', 1, 1, '30306', 'Male', 'White', '555-0505'),
(6, '303 Cedar St', 4, 3, '94102', 'Male', 'Other', '555-0606'),
(7, '404 Elm St', 3, 2, '10021', 'Female', 'White', '555-0707'),
(8, '505 Spruce Ave', 3, 2, '10022', 'Female', 'White', '555-0808'),
(9, '606 Willow Rd', 1, 1, '30307', 'Male', 'Other', '555-0909'),
(10,'707 Cherry Ln', 4, 3, '94103', 'Male', 'White', '555-1111');

-- EMPLOYEE_DIVISION
INSERT INTO employee_division (empid, div_id) VALUES
(1,1),(2,1),(3,2),(4,1),(5,2),(6,999),(7,3),(8,4),(9,1),(10,1);

-- EMPLOYEE_JOB_TITLES
INSERT INTO employee_job_titles (empid, job_title_id, start_date, end_date) VALUES
(1,100,'2022-08-01',NULL),(2,102,'2022-07-01',NULL),(3,101,'2022-07-03',NULL),
(4,100,'2022-08-02',NULL),(5,201,'2022-09-01',NULL),(6,900,'2020-01-01',NULL),
(7,200,'2023-01-15',NULL),(8,900,'2021-06-01',NULL),(9,100,'2024-03-01',NULL),
(10,102,'2024-03-01',NULL);

-- PAYROLL (one or more entries per employee)
INSERT INTO payroll (empid, pay_date, gross_pay, net_pay, month_year) VALUES
(1,'2025-01-31',865.38,700.00,'2025-01'),
(1,'2025-02-28',865.38,700.00,'2025-02'),
(2,'2025-01-31',923.08,747.00,'2025-01'),
(2,'2025-02-28',923.08,747.00,'2025-02'),
(3,'2025-01-31',1057.69,856.00,'2025-01'),
(3,'2025-02-28',1057.69,856.00,'2025-02'),
(4,'2025-01-31',1884.62,1520.00,'2025-01'),
(4,'2025-02-28',1884.62,1520.00,'2025-02'),
(5,'2025-01-31',826.92,660.00,'2025-01'),
(5,'2025-02-28',826.92,660.00,'2025-02'),
(6,'2025-01-31',1461.54,1180.00,'2025-01'),
(6,'2025-02-28',1461.54,1180.00,'2025-02'),
(7,'2025-01-31',1153.85,930.00,'2025-01'),
(7,'2025-02-28',1153.85,930.00,'2025-02'),
(8,'2025-01-31',1576.92,1280.00,'2025-01'),
(8,'2025-02-28',1576.92,1280.00,'2025-02'),
(9,'2025-01-31',1000.00,800.00,'2025-01'),
(9,'2025-02-28',1000.00,800.00,'2025-02'),
(10,'2025-01-31',1480.77,1200.00,'2025-01'),
(10,'2025-02-28',1480.77,1200.00,'2025-02');

SET FOREIGN_KEY_CHECKS=1;
