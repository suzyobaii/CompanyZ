package corelink;

public class ReportsService {
    private EmployeeService employeeService = new EmployeeService();
    private PayrollService payrollService = new PayrollService();

    public void listEmployeesByHireDate(String startDate, String endDate) {
        employeeService.searchEmployeesByHireDate(startDate, endDate);
    }
}
