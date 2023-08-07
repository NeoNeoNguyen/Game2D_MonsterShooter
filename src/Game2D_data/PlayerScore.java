package Game2D_data;

import Game2D_obj.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerScore {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/monster_shooter";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void addScore(String phoneNumber, int score, int timeInSeconds) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String insertQuery = "INSERT INTO score (create_time, phone_number, score, time) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setInt(3, score);
            preparedStatement.setInt(4, timeInSeconds);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<ScoreInfo> getLeaderboard() {
        List<ScoreInfo> leaderboard = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String selectQuery = "SELECT p.name, s.score, s.time "
                    + "FROM score s "
                    + "INNER JOIN player p ON s.phone_number = p.phone_number "
                    + "ORDER BY s.score DESC, s.time ASC "
                    + "LIMIT 10";  // Lấy 10 điểm cao nhất
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                int playerScore = resultSet.getInt("score");
                int playerTime = resultSet.getInt("time");
                leaderboard.add(new ScoreInfo(playerName, playerScore, playerTime));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    public static boolean login(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT COUNT(*) FROM player WHERE name = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            result.next();
            int count = result.getInt(1);

            conn.close();
            return count > 0; // Nếu count > 0, tức là thông tin đăng nhập hợp lệ

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xảy ra lỗi khi thực hiện đăng nhập
        }
    }

    public static boolean register(Player player) {
    try {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        // Kiểm tra xem phone_number đã tồn tại trong cơ sở dữ liệu chưa
        String checkQuery = "SELECT COUNT(*) FROM player WHERE phone_number = ?";
        PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
        checkStatement.setInt(1, player.getPhone_number()); // Sử dụng getPhone_number() của đối tượng Player
        ResultSet checkResult = checkStatement.executeQuery();
        checkResult.next();
        int count = checkResult.getInt(1);
        if (count > 0) {
            conn.close();
            return false; // phone number đã tồn tại, không thể đăng ký
        }

        // Thêm thông tin người chơi mới vào cơ sở dữ liệu
        String insertQuery = "INSERT INTO player (name, password, phone_number, highest_score, create_date) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
        insertStatement.setString(1, player.getName()); // Sử dụng getName() của đối tượng Player
        insertStatement.setString(2, player.getPassword()); // Sử dụng getPassword() của đối tượng Player
        insertStatement.setInt(3, player.getPhone_number()); // Sử dụng getPhone_number() của đối tượng Player
        insertStatement.setInt(4, player.getHighest_score()); // Sử dụng getHighest_score() của đối tượng Player
        insertStatement.setString(5, player.getCreate_date()); // Sử dụng getCreate_date() của đối tượng Player
        insertStatement.executeUpdate();

        conn.close();
        return true; // Đăng ký thành công

    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Xảy ra lỗi khi thực hiện đăng ký
    }
}


    public static class ScoreInfo {

        private String playerName;
        private int playerScore;
        private int playerTime;

        public ScoreInfo(String playerName, int playerScore, int playerTime) {
            this.playerName = playerName;
            this.playerScore = playerScore;
            this.playerTime = playerTime;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getPlayerScore() {
            return playerScore;
        }

        public int getPlayerTime() {
            return playerTime;
        }
    }
}