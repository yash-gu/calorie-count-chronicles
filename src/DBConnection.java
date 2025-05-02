import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/calorie_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "yash2006@@";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }
}

