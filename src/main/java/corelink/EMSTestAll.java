package corelink;

import java.sql.Date;
import java.util.List;

public class EMSTestAll {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeService();

        //1 - login
        boolean hrLogin = employeeService.login("hr_admin", "password123", "HR");
        boolean empLogin = employeeService.login("emp_user", "password123", "EMP");
        System.out.println("HR login: " + hrLogin);
        System.out.println("Employee login: " + empLogin);

        //2 - add new employee
        Date dob = Date.valueOf("1985-01-07");
        Date hireDate = Date.valueOf("2025-01-01");
        int empId = employeeService.addEmployee(
                "Lewis", "Hamilton", "1119789", dob, hireDate, 95000.0,
                "123 Main St", 1, 1, "30303", "M", "White", "555-0101"
        );
        System.out.println("Added Employee ID: " + empId);

        //3 - search employee by ID using searchEmployees
        List<Employee> empList = employeeService.searchEmployees(null, null, null, empId);
        if (!empList.isEmpty()) {
            Employee emp = empList.get(0);
            System.out.println("Found Employee: " + emp.getFirstName() + " " + emp.getLastName());
        }

        //4 - view employee by ID
        employeeService.viewEmployeeById(empId);

        //5 - update employee basic info
        employeeService.updateEmployeeBasic(empId, "Hamilton-Smith", 97000.0);

        //6 - update employee phone
        employeeService.updateEmployeePhone(empId, "555-0202");

        //7 - increase salary by range
        employeeService.increaseSalaryByRange(3.0, 58000.0, 105000.0);

        //8 - view pay history
        employeeService.viewPayHistory(empId);

        //9 - generate monthly report by job title
        employeeService.generateMonthlyReportByJobTitle("2025-12");

        //10 generate monthly report by division
        employeeService.generateMonthlyReportByDivision("2025-12");

        //11 - search employees by hire date
        employeeService.searchEmployeesByHireDate("2025-01-01", "2025-12-31");

        //12 - delete employee 
        // employeeService.deleteEmployee(empId);
    }
}
