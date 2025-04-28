package dao;

import models.Account;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AccountDAO {
    private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());
    
    public static Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getString("account_number"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting account by username", e);
            throw new SQLException("Could not retrieve account information", e);
        }
    }

    public static boolean transferMoney(String fromAccount, String toAccount, double amount, String description) 
            throws SQLException {
        if (fromAccount == null || toAccount == null || fromAccount.equals(toAccount) || amount <= 0) {
            throw new IllegalArgumentException("Invalid transfer parameters");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Verify accounts exist and have sufficient funds
            if (!accountExists(conn, toAccount)) {
                throw new SQLException("Recipient account does not exist");
            }
            
            if (getBalance(conn, fromAccount) < amount) {
                throw new SQLException("Insufficient funds for transfer");
            }
            
            // Perform transfer
            updateBalance(conn, fromAccount, -amount);
            updateBalance(conn, toAccount, amount);
            
            // Record transactions
            recordTransaction(conn, fromAccount, "Transfer to " + toAccount, -amount, "DEBIT");
            recordTransaction(conn, toAccount, "Transfer from " + fromAccount, amount, "CREDIT");
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.log(Level.SEVERE, "Transfer failed", e);
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public static boolean depositMoney(String accountNumber, double amount, String description) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");

        try (Connection conn = DBConnection.getConnection()) {
            if (!accountExists(conn, accountNumber)) {
                throw new SQLException("Account does not exist");
            }
            
            updateBalance(conn, accountNumber, amount);
            recordTransaction(conn, accountNumber, description, amount, "CREDIT");
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Deposit failed", e);
            throw e;
        }
    }

    public static boolean withdrawMoney(String accountNumber, double amount, String description) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");

        try (Connection conn = DBConnection.getConnection()) {
            double balance = getBalance(conn, accountNumber);
            if (balance < amount) {
                throw new SQLException("Insufficient funds");
            }
            
            updateBalance(conn, accountNumber, -amount);
            recordTransaction(conn, accountNumber, description, -amount, "DEBIT");
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Withdrawal failed", e);
            throw e;
        }
    }

    public static boolean createAccount(String username, String fullName, String accountType, 
                                      double initialDeposit, String phone, String email) throws SQLException {
        if (initialDeposit < 0) throw new IllegalArgumentException("Initial deposit cannot be negative");

        String sql = "INSERT INTO accounts (account_number, username, full_name, account_type, balance, phone, email) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        String accountNumber = generateAccountNumber();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            stmt.setString(2, username);
            stmt.setString(3, fullName);
            stmt.setString(4, accountType);
            stmt.setDouble(5, initialDeposit);
            stmt.setString(6, phone);
            stmt.setString(7, email);
            
            int rows = stmt.executeUpdate();
            
            if (rows > 0 && initialDeposit > 0) {
                recordTransaction(conn, accountNumber, "Initial deposit", initialDeposit, "CREDIT");
            }
            
            return rows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Account creation failed", e);
            throw e;
        }
    }

    // Helper methods
    private static String generateAccountNumber() {
        return "DB" + System.currentTimeMillis();
    }

    private static void recordTransaction(Connection conn, String accountNumber, 
                                        String description, double amount, String type) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, description, amount, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, description);
            stmt.setDouble(3, Math.abs(amount));
            stmt.setString(4, type);
            stmt.executeUpdate();
        }
    }

    private static boolean accountExists(Connection conn, String accountNumber) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static double getBalance(Connection conn, String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
                throw new SQLException("Account not found");
            }
        }
    }

    private static void updateBalance(Connection conn, String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Account update failed");
            }
        }
    }

    // Database verification method
    public static void verifyDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "accounts", null);
            
            if (!tables.next()) {
                JOptionPane.showMessageDialog(null, 
                    "Accounts table does not exist in database!", 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ResultSet columns = meta.getColumns(null, null, "accounts", null);
            StringBuilder sb = new StringBuilder("Accounts table columns:\n");
            
            while (columns.next()) {
                sb.append("- ").append(columns.getString("COLUMN_NAME"))
                  .append(" (").append(columns.getString("TYPE_NAME"))
                  .append(")\n");
            }
            
            JOptionPane.showMessageDialog(null, sb.toString(), 
                "Database Schema Info", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error verifying database: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}