package corelink;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

//Fix this later
public class EmployeeSearchTest {
    private final EmployeeService employeeService = new EmployeeService();
    private final int empid = 1;

    @Test
    public void getEmployeeById_Success() {
        Employee emp = employeeService.getEmployeeById(empid);
        assertTrue(emp != null);
    }
}
