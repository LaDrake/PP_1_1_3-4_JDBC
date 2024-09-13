package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/katabd";
    private static final String DB_USERNAME = "root1";
    private static final String DB_PASSWORD = "13243512s";
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the database successfully");
        } catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
