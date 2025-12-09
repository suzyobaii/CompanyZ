package corelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthService {
    private Connection conn;

    public UserAuthService(Connection conn) {
        this.conn = conn;
    }

    public String login(String username, String password) throws SQLException {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // e.g., "HR_ADMIN" or "EMPLOYEE"
                } else {
                    return null; // invalid credentials
                }
            }
        }
    }
}
