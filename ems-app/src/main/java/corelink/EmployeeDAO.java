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

    //authentication 
    public boolean authenticate(String username, String password, String role) {
        return (username.equals("hr_admin") && password.equals("password123") && role.equals("HR")) ||
               (username.equals("emp_user") && password.equals("password123") && role.equals("EMP"));
    }

    //search employees
    public List<Employee> searchEmployees(String firstName, String lastName, String ssn, Integer empId) {
        List<Employee> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.empid, e.first_name, e.last_name, e.ssn, e.dob, e.hire_date, e.base_salary, e.status, a.phone " +
            "FROM employees e LEFT JOIN address a ON e.empid = a.empid WHERE 1=1 "
        );

        if (firstName != null && !firstName.isEmpty()) sql.append("AND e.first_name LIKE ? ");
        if (lastName != null && !lastName.isEmpty()) sql.append("AND e.last_name LIKE ? ");
        if (ssn != null && !ssn.isEmpty()) sql.append("AND e.ssn = ? ");
        if (empId != null) sql.append("AND e.empid = ? ");

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (firstName != null && !firstName.isEmpty()) ps.setString(idx++, firstName + "%");
            if (lastName != null && !lastName.isEmpty()) ps.setString(idx++, lastName + "%");
            if (ssn != null && !ssn.isEmpty()) ps.setString(idx++, ssn);
            if (empId != null) ps.setInt(idx++, empId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee(
                    rs.getInt("empid"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("ssn"),
                    rs.getDate("dob"),
                    rs.getDate("hire_date"),
                    rs.getDouble("base_salary"),
                    rs.getString("status"),
                    rs.getString("phone")
                );
                results.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error in searchEmployees: " + ex.getMessage());
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
            System.out.println("Employee with this SSN already exists.");
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

            String sqlAddr = "INSERT INTO address (empid, street, city_id, state_id, zip, gender, identified_race, phone) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psAddr = conn.prepareStatement(sqlAddr)) {
                psAddr.setInt(1, empId);
                psAddr.setString(2, street);
                psAddr.setInt(3, cityId);
                psAddr.setInt(4, stateId);
                psAddr.setString(5, zip);
                psAddr.setString(6, gender);
                psAddr.setString(7, identifiedRace);
                psAddr.setString(8, phone);
                psAddr.executeUpdate();
            }

        } catch (SQLException ex) {
            System.out.println("Error in addEmployee: " + ex.getMessage());
        }

        return empId;
    }

    //update Employee basicccc
    public void updateEmployeeBasic(int empId, String newLastName, double newSalary) {
        String sql = "UPDATE employees SET last_name = ?, base_salary = ? WHERE empid = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newLastName);
            ps.setDouble(2, newSalary);
            ps.setInt(3, empId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in updateEmployeeBasic: " + ex.getMessage());
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
            System.out.println("Error in updatePhone: " + ex.getMessage());
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
            System.out.println("Error in increaseSalaryByRange: " + ex.getMessage());
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
            System.out.println("Error in deleteEmployee: " + ex.getMessage());
        }
    }

    // print employee by ID
    public void printEmployeeById(int empId) {
        String sql = "SELECT e.*, a.phone FROM employees e LEFT JOIN address a ON e.empid = a.empid WHERE e.empid = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Employee: " + rs.getInt("empid") + ", " +
                                   rs.getString("first_name") + " " + rs.getString("last_name") +
                                   ", SSN: " + rs.getString("ssn") +
                                   ", Phone: " + rs.getString("phone"));
            }
        } catch (SQLException ex) {
            System.out.println("Error in printEmployeeById: " + ex.getMessage());
        }
    }

    //Pay Reports / history
    public void getPayHistory(int empId) {
        String sql = "SELECT * FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            System.out.println("Pay History for empid " + empId + ":");
            while (rs.next()) {
                System.out.println("Pay Date: " + rs.getDate("pay_date") +
                                   ", Gross: " + rs.getDouble("gross_pay") +
                                   ", Net: " + rs.getDouble("net_pay"));
            }
        } catch (SQLException ex) {
            System.out.println("Error in getPayHistory: " + ex.getMessage());
        }
    }

    // monthly pay by job title
   public void getMonthlyTotalsByJobTitle(String monthYear) {
        // monthYear example: "2025-12"
        String startDate = monthYear + "-01";
        String endDate = monthYear + "-31";

        String sql = "SELECT jt.title_name, SUM(ps.gross_pay) AS total_pay " +
                    "FROM payroll ps " +
                    "JOIN employees e ON ps.empid = e.empid " +
                    "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                    "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                    "WHERE ps.pay_date BETWEEN ? AND ? " +
                    "GROUP BY jt.title_name";

        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            System.out.println("Monthly Pay by Job Title (" + monthYear + "):");
            while (rs.next()) {
                System.out.println(rs.getString("title_name") + ": " + rs.getDouble("total_pay"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in getMonthlyTotalsByJobTitle: " + ex.getMessage());
        }
    }

    //monthly pay by division
   public void getMonthlyTotalsByDivision(String monthYear) {
        String startDate = monthYear + "-01";
        String endDate = monthYear + "-31";

        String sql = "SELECT d.name, SUM(ps.gross_pay) AS total_pay " +
                    "FROM payroll ps " +
                    "JOIN employees e ON ps.empid = e.empid " +
                    "JOIN employee_division ed ON e.empid = ed.empid " +
                    "JOIN division d ON ed.div_id = d.id " +
                    "WHERE ps.pay_date BETWEEN ? AND ? " +
                    "GROUP BY d.name";

        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            System.out.println("Monthly Pay by Division (" + monthYear + "):");
            while (rs.next()) {
                System.out.println(rs.getString("name") + ": " + rs.getDouble("total_pay"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in getMonthlyTotalsByDivision: " + ex.getMessage());
        }
    }
    //search by hire date range
    public void searchEmployeesByHireDate(String startDate, String endDate) {
        String sql = "SELECT e.empid, e.first_name, e.last_name, e.hire_date " +
                     "FROM employees e WHERE e.hire_date BETWEEN ? AND ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Employee: " + rs.getInt("empid") + ", " +
                                   rs.getString("first_name") + " " + rs.getString("last_name") +
                                   ", Hire Date: " + rs.getDate("hire_date"));
            }
        } catch (SQLException ex) {
            System.out.println("Error in searchEmployeesByHireDate: " + ex.getMessage());
        }
    }
}