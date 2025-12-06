package companyz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    // Pay history for one employee, newest first
    public void printPayHistory(int empid) {
        String sql = "SELECT pay_date, gross_pay, net_pay " +
                     "FROM payroll WHERE empid = ? ORDER BY pay_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empid);
            ResultSet rs = ps.executeQuery();

            System.out.println("Pay history for empid = " + empid);
            while (rs.next()) {
                System.out.printf("Date: %s | Gross: %.2f | Net: %.2f%n",
                        rs.getDate("pay_date"),
                        rs.getDouble("gross_pay"),
                        rs.getDouble("net_pay"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in printPayHistory: " + ex.getMessage());
        }
    }

    // Total pay for month by job title
    public void printTotalPayByJobTitle(String monthYear) {
        String sql = "SELECT jt.title_name, SUM(p.gross_pay) AS total_gross " +
                     "FROM payroll p " +
                     "JOIN employees e ON p.empid = e.empid " +
                     "JOIN employee_job_titles ej ON e.empid = ej.empid " +
                     "JOIN job_titles jt ON ej.job_title_id = jt.job_title_id " +
                     "WHERE p.month_year = ? " +
                     "GROUP BY jt.title_name";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            System.out.println("Total pay for month " + monthYear + " by job title:");
            while (rs.next()) {
                System.out.printf("%s: %.2f%n",
                        rs.getString("title_name"),
                        rs.getDouble("total_gross"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in printTotalPayByJobTitle: " + ex.getMessage());
        }
    }

    // Total pay for month by division
    public void printTotalPayByDivision(String monthYear) {
        String sql = "SELECT d.name AS division_name, SUM(p.gross_pay) AS total_gross " +
                     "FROM payroll p " +
                     "JOIN employees e ON p.empid = e.empid " +
                     "JOIN employee_division ed ON e.empid = ed.empid " +
                     "JOIN division d ON ed.div_id = d.id " +
                     "WHERE p.month_year = ? " +
                     "GROUP BY d.name";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monthYear);
            ResultSet rs = ps.executeQuery();

            System.out.println("Total pay for month " + monthYear + " by division:");
            while (rs.next()) {
                System.out.printf("%s: %.2f%n",
                        rs.getString("division_name"),
                        rs.getDouble("total_gross"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in printTotalPayByDivision: " + ex.getMessage());
        }
    }
}
