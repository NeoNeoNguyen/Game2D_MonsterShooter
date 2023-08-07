package Game2D_data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() {
        Connection c = null;

        final String url = "jdbc:mysql://localhost:3306/monster_shooter";
        final String user = "root";
        final String password = "";
        try {
            c = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("SQLException" + e);
        }
        return c;
    }
}
