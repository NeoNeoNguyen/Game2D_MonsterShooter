package Game2D_data;

import Game2D_obj.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPlayer {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int st;

    public Player checkLogin(String uname) {
        Player student = new Player();
        con = ConnectionFactory.getConnection();
        try {
            String query = "select* from player where phone_number=?";
            ps = con.prepareStatement(query);
            ps.setString(1, uname);
            rs = ps.executeQuery();
            while (rs.next()) {
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setPhone_number(rs.getString("phone_number"));
                student.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return student;
    }
}
