package dao;

import java.sql.*;

public class UserDAO {

    public static boolean register(String username, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            int rows = pst.executeUpdate();
            conn.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String username, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            boolean found = rs.next();
            conn.close();
            return found;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
