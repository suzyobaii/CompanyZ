package corelink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database { //change url when using employeeDataTest
    private static final String URL = "jdbc:mysql://localhost:3306/employeeData?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "password"; //change to your MySQL password whrn u run

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
