package corelink;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDAOTest {

    private EmployeeDAO empDAO;
    private int testEmpid;

    @BeforeAll
    public void setUp() throws SQLException {
        empDAO = new EmployeeDAO();

        testEmpid = empDAO.addEmployee(
        "John", 
        "Doe", 
        "123456789", 
        Date.valueOf("2001-01-01"), 
        Date.valueOf("2012-12-12"), 
        85000.00, 
        "777 Clover Ln", 
        7, 
        49, 
        "12345", 
        "Female", 
        "Mixed", 
        "123-456-7890");
    }

    @AfterAll
    public void shutDown() throws SQLException {
        empDAO.deleteEmployee(testEmpid);
    }

    //====================== Testing updateEmployeeBasic() ==========================

    @Test
    @DisplayName("Update Employee Success")
    public void testUpdateEmployeeBasic_Success() {
        String newLastName = "Dane";
        double newSalary = 75000.00;

        empDAO.updateEmployeeBasic(testEmpid, newLastName, newSalary);

        List<Employee> res = empDAO.searchEmployees(null, null, null, testEmpid);
        
        if (res.isEmpty()) {
            fail("Test built wrong, check if test employee was created correctly.");
        }

        Employee updatedEmployee = res.get(0);

        assertNotNull(updatedEmployee);
        assertEquals(newLastName, updatedEmployee.getLastName());
        assertEquals(newSalary, updatedEmployee.getBaseSalary());
    }

    @Test
    @DisplayName("Update Employee with empid that doesn't exist")
    public void testUpdateEmployeeBasic_FalseEmployee() {
        int falseEmpid = 999; // employee that doesn't exist
        String newLastName = "Joe";
        double newSalary = 65000.00;

        empDAO.updateEmployeeBasic(falseEmpid, newLastName, newSalary);
        List<Employee> res = empDAO.searchEmployees(null, null, null, testEmpid);

        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Update Employee with Negative Salary")
    public void testUpdateEmployeeBasic_NegativeSalary() {
        String newLastName = "Joe";
        double newSalary = -1000.00;

        empDAO.updateEmployeeBasic(testEmpid, newLastName, newSalary);
        
        List<Employee> res = empDAO.searchEmployees(null, null, null, testEmpid);

        if (res.isEmpty()) {
            fail("Test built wrong, check if test employee was created correctly.");
        }
        
        Employee updatedEmployee = res.get(0);

        assertNotNull(updatedEmployee);
        assertEquals(newLastName, updatedEmployee.getLastName());
        assertEquals(newSalary, updatedEmployee.getBaseSalary());
    }

    //============================ Testing updatePhone() =========================
    

    
}
