/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtils {
    private static final String DB_NAME = "ECommerceDB";
    private static final String DB_USER_NAME = "sa";
    private static final String DB_PASSWORD = "12345";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
        Connection con = DriverManager.getConnection(url, DB_USER_NAME, DB_PASSWORD);
        return con;
    }
}
