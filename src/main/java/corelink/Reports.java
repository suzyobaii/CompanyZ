package corelink;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Reports {

    //print employees hired between two dates
    public void printEmployeesHiredBetween(LocalDate start, LocalDate end) {
        String sql = "SELECT empid, first_name, last_name, hire_date " +
                     "FROM employees " +
                     "WHERE hire_date BETWEEN ? AND ? " +
                     "ORDER BY hire_date";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));

            ResultSet rs = ps.executeQuery();

            System.out.printf("Employees hired between %s and %s:%n", start, end);

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("empid=%d | %s %s | hire_date=%s%n",
                        rs.getInt("empid"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("hire_date"));
            }

            if (!found) {
                System.out.println("No employees found in this date range.");
            }

        } catch (SQLException ex) {
            System.out.println("Error in printEmployeesHiredBetween: " + ex.getMessage());
        }
    }
}
