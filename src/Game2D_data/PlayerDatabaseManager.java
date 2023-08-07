//import Game2D_obj.Player;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;s
//
//public class PlayerDatabaseManager {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/monster_shooter";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "";
//
//    public static boolean registerPlayer(Player player) {
//        try {
//            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            String query = "INSERT INTO players (name, phone_number, password, highest_score) VALUES (?, ?, ?, ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, player.getName());
//            statement.setInt(2, player.getPhone_number());
//            statement.setString(3, player.getPassword());
//            statement.setInt(4, player.getHighest_score());
//            
//            int rowsAffected = statement.executeUpdate();
//            statement.close();
//            connection.close();
//            
//            return rowsAffected > 0; // Registration successful if rows were affected
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false; // Registration failed
//        }
//    }
//}
