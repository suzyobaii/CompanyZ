package companyz;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final PayrollDAO payrollDAO = new PayrollDAO();
    private static final Reports reports = new Reports();

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== Company Z Employee Management System ===");
            System.out.println("1. HR Admin Login");
            System.out.println("2. Employee Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (hrLogin()) {
                        hrMenu();
                    }
                    break;
                case "2":
                    Integer empid = employeeLogin();
                    if (empid != null) {
                        employeeMenu(empid);
                    }
                    break;
                case "0":
                    System.out.println("Goodbye.");
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }

    // Simple hard-coded HR admin login (you could put this in a table later)
    private static boolean hrLogin() {
        System.out.print("Enter HR admin username: ");
        String user = scanner.nextLine();
        System.out.print("Enter HR admin password: ");
        String pass = scanner.nextLine();

        if ("admin".equals(user) && "admin123".equals(pass)) {
            System.out.println("HR admin login successful.\n");
            return true;
        } else {
            System.out.println("Invalid HR admin credentials.\n");
            return false;
        }
    }

    // Employee login by empid + last name
    private static Integer employeeLogin() {
        System.out.print("Enter your Employee ID: ");
        String idStr = scanner.nextLine();
        int empid;
        try {
            empid = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid empid.\n");
            return null;
        }

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        List<Employee> list = employeeDAO.searchEmployees(null, lastName, null, empid);
        if (list.isEmpty()) {
            System.out.println("No matching employee found.\n");
            return null;
        } else {
            System.out.println("Welcome, " + list.get(0).getFirstName() + " " + list.get(0).getLastName() + "!\n");
            return empid;
        }
    }

    // HR admin menu (CRUD + reports)
    private static void hrMenu() {
        while (true) {
            System.out.println("=== HR Admin Menu ===");
            System.out.println("1. Search Employee");
            System.out.println("2. Add New Employee");
            System.out.println("3. Update Employee Basic Data");
            System.out.println("4. Increase Salary by % in Range");
            System.out.println("5. Reports");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    hrSearchEmployee();
                    break;
                case "2":
                    hrAddEmployee();
                    break;
                case "3":
                    hrUpdateEmployeeBasic();
                    break;
                case "4":
                    hrIncreaseSalary();
                    break;
                case "5":
                    hrReportsMenu();
                    break;
                case "0":
                    System.out.println("HR admin logged out.\n");
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }

    private static void hrSearchEmployee() {
        System.out.print("First name (or blank): ");
        String fn = scanner.nextLine();
        System.out.print("Last name (or blank): ");
        String ln = scanner.nextLine();
        System.out.print("SSN (9 digits or blank): ");
        String ssn = scanner.nextLine();
        System.out.print("EmpID (or blank): ");
        String empStr = scanner.nextLine();
        Integer empid = null;
        if (!empStr.isEmpty()) {
            try {
                empid = Integer.parseInt(empStr);
            } catch (NumberFormatException ex) {
                System.out.println("Ignoring invalid empid input.");
            }
        }

        List<Employee> results = employeeDAO.searchEmployees(
                fn.isEmpty() ? null : fn,
                ln.isEmpty() ? null : ln,
                ssn.isEmpty() ? null : ssn,
                empid
        );

        if (results.isEmpty()) {
            System.out.println("No employees found.\n");
        } else {
            System.out.println("Search results:");
            for (Employee e : results) {
                System.out.println(e);
            }
            System.out.println();
        }
    }

    private static void hrAddEmployee() {
        try {
            System.out.print("First name: ");
            String fn = scanner.nextLine();
            System.out.print("Last name: ");
            String ln = scanner.nextLine();
            System.out.print("SSN (9 digits): ");
            String ssn = scanner.nextLine();
            System.out.print("DOB (YYYY-MM-DD): ");
            String dobStr = scanner.nextLine();
            System.out.print("Hire Date (YYYY-MM-DD): ");
            String hireStr = scanner.nextLine();
            System.out.print("Base Salary: ");
            double salary = Double.parseDouble(scanner.nextLine());

            int newEmpid = employeeDAO.addEmployee(
                    fn,
                    ln,
                    ssn,
                    Date.valueOf(dobStr),
                    Date.valueOf(hireStr),
                    salary
            );

            if (newEmpid > 0) {
                System.out.println("New employee created with empid = " + newEmpid + "\n");
            } else {
                System.out.println("Failed to create employee.\n");
            }

        } catch (Exception ex) {
            System.out.println("Error adding employee: " + ex.getMessage() + "\n");
        }
    }

    private static void hrUpdateEmployeeBasic() {
        try {
            System.out.print("EmpID to update: ");
            int empid = Integer.parseInt(scanner.nextLine());
            System.out.print("New last name: ");
            String newLn = scanner.nextLine();
            System.out.print("New base salary: ");
            double newSalary = Double.parseDouble(scanner.nextLine());

            employeeDAO.updateEmployeeBasic(empid, newLn, newSalary);
            System.out.println();

        } catch (Exception ex) {
            System.out.println("Error updating employee: " + ex.getMessage() + "\n");
        }
    }

    private static void hrIncreaseSalary() {
        try {
            System.out.print("Percent increase (e.g., 3.2): ");
            double percent = Double.parseDouble(scanner.nextLine());
            System.out.print("Min salary (inclusive): ");
            double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Max salary (exclusive): ");
            double max = Double.parseDouble(scanner.nextLine());

            employeeDAO.increaseSalaryByRange(percent, min, max);
            System.out.println();

        } catch (Exception ex) {
            System.out.println("Error increasing salary: " + ex.getMessage() + "\n");
        }
    }

    private static void hrReportsMenu() {
        while (true) {
            System.out.println("=== HR Reports Menu ===");
            System.out.println("1. Total pay for month by job title");
            System.out.println("2. Total pay for month by division");
            System.out.println("3. Employees hired within a date range");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter month (YYYY-MM): ");
                    String m1 = scanner.nextLine();
                    payrollDAO.printTotalPayByJobTitle(m1);
                    System.out.println();
                    break;
                case "2":
                    System.out.print("Enter month (YYYY-MM): ");
                    String m2 = scanner.nextLine();
                    payrollDAO.printTotalPayByDivision(m2);
                    System.out.println();
                    break;
                case "3":
                    try {
                        System.out.print("Start date (YYYY-MM-DD): ");
                        LocalDate start = LocalDate.parse(scanner.nextLine());
                        System.out.print("End date (YYYY-MM-DD): ");
                        LocalDate end = LocalDate.parse(scanner.nextLine());
                        reports.printEmployeesHiredBetween(start, end);
                        System.out.println();
                    } catch (Exception ex) {
                        System.out.println("Invalid date.\n");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }

    // Employee menu (view-only)
    private static void employeeMenu(int empid) {
        while (true) {
            System.out.println("=== Employee Menu ===");
            System.out.println("1. View My Personal Data");
            System.out.println("2. View My Pay Statement History");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewMyData(empid);
                    break;
                case "2":
                    payrollDAO.printPayHistory(empid);
                    System.out.println();
                    break;
                case "0":
                    System.out.println("Employee logged out.\n");
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }

    private static void viewMyData(int empid) {
        List<Employee> list = employeeDAO.searchEmployees(null, null, null, empid);
        if (list.isEmpty()) {
            System.out.println("No data found.\n");
        } else {
            System.out.println(list.get(0));
            System.out.println();
        }
    }
}
