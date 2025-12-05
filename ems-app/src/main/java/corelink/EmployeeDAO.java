package companyz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Search by various fields (HR admin / employee)
    public List<Employee> searchEmployees(String firstName, String lastName,
                                          String ssn, Integer empid) {
        List<Employee> results = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT empid, first_name, last_name, ssn, dob, hire_date, base_salary, status " +
                "FROM employees WHERE 1=1 "
        );

        if (firstName != null && !firstName.isEmpty()) {
            sql.append("AND first_name LIKE ? ");
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql.append("AND last_name LIKE ? ");
        }
        if (ssn != null && !ssn.isEmpty()) {
            sql.append("AND ssn = ? ");
        }
        if (empid != null) {
            sql.append("AND empid = ? ");
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (firstName != null && !firstName.isEmpty()) {
                ps.setString(idx++, firstName + "%");
            }
            if (lastName != null && !lastName.isEmpty()) {
                ps.setString(idx++, lastName + "%");
            }
            if (ssn != null && !ssn.isEmpty()) {
                ps.setString(idx++, ssn);
            }
            if (empid != null) {
                ps.setInt(idx++, empid);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("empid"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("ssn"),
                        rs.getString("dob"),
                        rs.getString("hire_date"),
                        rs.getDouble("base_salary"),
                        rs.getString("status")
                );
                results.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error in searchEmployees: " + ex.getMessage());
        }

        return results;
    }

    // Add new employee (basic info only for now)
    public int addEmployee(String firstName, String lastName, String ssn,
                           Date dob, Date hireDate, double baseSalary) {
        String sql = "INSERT INTO employees " +
                     "(first_name, last_name, ssn, dob, hire_date, base_salary, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, ssn);
            ps.setDate(4, dob);
            ps.setDate(5, hireDate);
            ps.setDouble(6, baseSalary);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                System.out.println("No rows inserted.");
                return -1;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error in addEmployee: " + ex.getMessage());
        }

        return -1;
    }

    // Update general employee data (simple example: last name + salary)
    public void updateEmployeeBasic(int empid, String newLastName, double newSalary) {
        String sql = "UPDATE employees SET last_name = ?, base_salary = ? WHERE empid = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newLastName);
            ps.setDouble(2, newSalary);
            ps.setInt(3, empid);

            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " row(s).");

        } catch (SQLException ex) {
            System.out.println("Error in updateEmployeeBasic: " + ex.getMessage());
        }
    }

    // Increase salary by % for salary range [min, max)
    public void increaseSalaryByRange(double percent, double minSalary, double maxSalary) {
        String sql = "UPDATE employees " +
                     "SET base_salary = base_salary * (1 + ? / 100.0) " +
                     "WHERE base_salary >= ? AND base_salary < ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, percent);
            ps.setDouble(2, minSalary);
            ps.setDouble(3, maxSalary);

            int rows = ps.executeUpdate();
            System.out.println("Updated salaries for " + rows + " employee(s).");

        } catch (SQLException ex) {
            System.out.println("Error in increaseSalaryByRange: " + ex.getMessage());
        }
    }
}
