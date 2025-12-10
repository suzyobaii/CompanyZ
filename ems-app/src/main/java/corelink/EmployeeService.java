package corelink;

import java.sql.Date;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    //login 
    public boolean login(String username, String password, String role) {
        return employeeDAO.authenticate(username, password, role);
    }

    //search employees 
    public List<Employee> searchEmployees(String firstName, String lastName, String ssn, Integer empId) {
        return employeeDAO.searchEmployees(firstName, lastName, ssn, empId);
    }

    //add employee 
    public int addEmployee(String firstName, String lastName, String ssn,
                           Date dob, Date hireDate, double baseSalary,
                           String street, int cityId, int stateId, String zip,
                           String gender, String identifiedRace, String phone) {
        return employeeDAO.addEmployee(firstName, lastName, ssn, dob, hireDate, baseSalary,
                                       street, cityId, stateId, zip, gender, identifiedRace, phone);
    }

    //update employee basic 
    public void updateEmployeeBasic(int empId, String newLastName, double newSalary) {
        employeeDAO.updateEmployeeBasic(empId, newLastName, newSalary);
    }

    //update employee phone 
    public void updateEmployeePhone(int empId, String newPhone) {
        employeeDAO.updatePhone(empId, newPhone);
    }

    //increase salary by range 
    public void increaseSalaryByRange(double percent, double minSalary, double maxSalary) {
        employeeDAO.increaseSalaryByRange(percent, minSalary, maxSalary);
    }

    //delete employee 
    public void deleteEmployee(int empId) {
        employeeDAO.deleteEmployee(empId);
    }

    //view employee by ID 
    public void viewEmployeeById(int empId) {
        employeeDAO.printEmployeeById(empId);
    }

    //view Pay History 
    public void viewPayHistory(int empId) {
        employeeDAO.getPayHistory(empId);
    }

    //generate monthly report by job title 
    public void generateMonthlyReportByJobTitle(String monthYear) {
        employeeDAO.getMonthlyTotalsByJobTitle(monthYear);
    }

    //generate monthly report by division 
    public void generateMonthlyReportByDivision(String monthYear) {
        employeeDAO.getMonthlyTotalsByDivision(monthYear);
    }

    //search employees by hire date 
    public void searchEmployeesByHireDate(String startDate, String endDate) {
        employeeDAO.searchEmployeesByHireDate(startDate, endDate);
    }
}