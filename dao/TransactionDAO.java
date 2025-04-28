package dao;

import models.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/digital_banking";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    public static List<Transaction> getRecentTransactions(String accountNumber, int limit) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC LIMIT ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    rs.getString("account_number"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getString("type"),
                    rs.getTimestamp("transaction_date")
                ));
            }
        }
        return transactions;
    }
}