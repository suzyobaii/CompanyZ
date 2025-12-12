package corelink;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private final AddressService addressService = new AddressService();

    //search employees
    public List<Employee> searchEmployees(String firstName, String lastName, String ssn, Integer empId) {
        List<Employee> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM employees WHERE 1=1 "
        );

        if (firstName != null && !firstName.isEmpty()) sql.append("AND first_name LIKE ? ");
        if (lastName != null && !lastName.isEmpty()) sql.append("AND last_name LIKE ? ");
        if (ssn != null && !ssn.isEmpty()) sql.append("AND ssn = ? ");
        if (empId != null) sql.append("AND empid = ? ");

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (firstName != null && !firstName.isEmpty()) ps.setString(idx++, firstName + "%");
            if (lastName != null && !lastName.isEmpty()) ps.setString(idx++, lastName + "%");
            if (ssn != null && !ssn.isEmpty()) ps.setString(idx++, ssn);
            if (empId != null) ps.setInt(idx++, empId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                    e.setEmpid(rs.getInt("empid"));
                    e.setFirstName(rs.getString("first_name"));
                    e.setLastName(rs.getString("last_name"));
                    e.setSSN(rs.getString("ssn"));
                    e.setDOB(rs.getDate("dob"));
                    e.setHireDate(rs.getDate("hire_date"));
                    e.setBaseSalary(rs.getDouble("base_salary"));
                    e.setStatus(rs.getString("status"));
                results.add(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error in searchEmployees: " + ex.getMessage());
        }
        return results;
    }

    //add Employee (with Address ~)
    public int addEmployee(String firstName, String lastName, String ssn,
                           Date dob, Date hireDate, double baseSalary,
                           String street, int cityId, int stateId, String zip,
                           String gender, String identifiedRace, String phone) {

        int empId = -1;
        List<Employee> duplicates = searchEmployees(null, null, ssn, null);
        if (!duplicates.isEmpty()) {
            return -1;
        }

        String sqlEmp = "INSERT INTO employees (first_name, last_name, ssn, dob, hire_date, base_salary, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = Database.getConnection();
             PreparedStatement psEmp = conn.prepareStatement(sqlEmp, Statement.RETURN_GENERATED_KEYS)) {

            psEmp.setString(1, firstName);
            psEmp.setString(2, lastName);
            psEmp.setString(3, ssn);
            psEmp.setDate(4, dob);
            psEmp.setDate(5, hireDate);
            psEmp.setDouble(6, baseSalary);

            int affected = psEmp.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = psEmp.getGeneratedKeys()) {
                if (keys.next()) empId = keys.getInt(1);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error in addEmployee: " + ex.getMessage());
        }

        // Add address
        addressService.addAddress(empId, street, cityId, stateId, zip, gender, identifiedRace, phone);

        return empId;
    }

    //update Employee basic
    public void updateEmployeeBasic(int empId, String newLastName, double newSalary) {
        String sql = "UPDATE employees SET last_name = ?, base_salary = ? WHERE empid = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newLastName);
            ps.setDouble(2, newSalary);
            ps.setInt(3, empId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in updateEmployeeBasic: " + ex.getMessage());
        }
    }

    //update Phone
    public void updatePhone(int empId, String phone) {
        String sql = "UPDATE address SET phone = ? WHERE empid = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, empId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in updatePhone: " + ex.getMessage());
        }
    }

    //increase Salary by Range
    public void increaseSalaryByRange(double percent, double minSalary, double maxSalary) {
        String sql = "UPDATE employees SET base_salary = base_salary * (1 + ? / 100) WHERE base_salary >= ? AND base_salary <= ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, percent);
            ps.setDouble(2, minSalary);
            ps.setDouble(3, maxSalary);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in increaseSalaryByRange: " + ex.getMessage());
        }
    }

    //delete Employee
    public void deleteEmployee(int empId) {
        String sql = "DELETE FROM employees WHERE empid = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in deleteEmployee: " + ex.getMessage());
        }
    }

    // print employee by ID
    public Employee getEmployeeById(int empId) {
        String sql = "SELECT * FROM employees WHERE empid = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = new Employee();
                e.setEmpid(rs.getInt("empid"));
                e.setFirstName(rs.getString("first_name"));
                e.setLastName(rs.getString("last_name"));
                e.setSSN(rs.getString("ssn"));
                e.setDOB(rs.getDate("dob"));
                e.setHireDate(rs.getDate("hire_date"));
                e.setBaseSalary(rs.getDouble("base_salary"));
                e.setStatus(rs.getString("status"));
                
                return e;
            }
            else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error in getEmployeeById: " + ex.getMessage());
        }
    }
}