package corelink;


public class UserAuthService {
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    private final EmployeeService employeeService = new EmployeeService();

    public boolean hrLogin(String username, String password) {
        return (adminUsername.equals(username)) && (adminPassword.equals(password));
    }

    public boolean employeeLogin(int empid, String ssn) {
        Employee employee = employeeService.getEmployeeById(empid);
        return (employee != null) && (employee.getSSN().equals(ssn));
    }
}
