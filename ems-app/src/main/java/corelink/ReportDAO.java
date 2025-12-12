package corelink;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    //Pay Reports / history
    public String getPayHistory(int empId) {
        StringBuilder report = new StringBuilder();
        String sql = "SELECT * FROM payroll WHERE empid = ? ORDER BY pay_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "No Pay History data found for " + empId + ".";
            }

            report.append("Pay History for empid ").append(empId).append(":\n");
            report.append("=========================================\n");
            do {
                report.append("\nPay Date: ")
                .append(rs.getDate("pay_date"))
                .append(" | Gross: $")
                .append(String.format("%.2f", rs.getDouble("gross_pay")))
                .append(" | Net: $")
                .append(String.format("%.2f", rs.getDouble("net_pay")));
            } while (rs.next());

        } catch (SQLException ex) {
            return "Error in getPayHistory: " + ex.getMessage();
        }

        return report.toString();
    }

    // monthly pay by job title
   public String getMonthlyTotalByJobTitle(String monthYear) {
        StringBuilder report = new StringBuilder();
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

            if (!rs.next()) {
                return "No payroll data found for " + monthYear + ".";
            }

            report.append("Monthly Pay by Job Title ").append(monthYear).append(":\n");
            report.append("=========================================\n");
            do {
                report.append(rs.getString("title_name"))
                .append(": $")
                .append(String.format("%.2f", rs.getDouble("total_pay")))
                .append("\n");
            } while (rs.next());

        } catch (SQLException ex) {
            return "Error in getMonthlyTotalByJobTitle: " + ex.getMessage();
        }
        return report.toString();
    }

    //monthly pay by division
    public String getMonthlyTotalByDivision(String monthYear) {
        StringBuilder report = new StringBuilder();
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
            
            if (!rs.next()) {
                return "No payroll data found for " + monthYear + ".";
            }
            
            report.append("Monthly Pay by Division ").append(monthYear).append(":\n");
            report.append("=========================================\n");
            
            do {
                report.append(rs.getString("name"))
                      .append(": $")
                      .append(String.format("%.2f", rs.getDouble("total_pay")))
                      .append("\n");
            } while (rs.next());
            
        } catch (SQLException ex) {
            return "Error in getMonthlyTotalByDivision: " + ex.getMessage();
        }
        
        return report.toString();
    }
    
    //print employees hired between two dates
    public String getEmployeesHiredBetween(String start, String end) {
        StringBuilder report = new StringBuilder();
        String sql = "SELECT empid, first_name, last_name, hire_date " +
                     "FROM employees " +
                     "WHERE hire_date BETWEEN ? AND ? " +
                     "ORDER BY hire_date";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "No Employee data found for " + start + "to " + end + ".";
            }

            report.append("Employees Hired Between ").append(start).append(" to ").append(end).append("\n");
            report.append("=========================================\n");


            do {
                report.append("\nEmployee ID: ")
                .append(rs.getInt("empid"))
                .append(" | First Name: ")
                .append(rs.getString("first_name"))
                .append(" | Last Name: ")
                .append(rs.getString("last_name"))
                .append(" | Hire Date: ")
                .append(rs.getDate("hire_date"));
            } while (rs.next());

        } catch (SQLException ex) {
            return "Error in getEmployeesByHireDate: " + ex.getMessage();
        }
        return report.toString();
    }
}
