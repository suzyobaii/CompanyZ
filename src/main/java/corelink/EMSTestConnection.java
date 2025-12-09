package corelink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EMSTestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/employeeData?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root"; 
        String password = "Kj.1119789";

        try {
            //load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //connect to database
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to employeeData database successfully!");

            //close connection
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
