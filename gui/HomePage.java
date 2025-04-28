package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.AccountDAO;
import dao.TransactionDAO;
import models.Account;
import models.Transaction;

public class HomePage extends JFrame {

    private String username;
    private JLabel balanceLabel;
    private JLabel welcomeLabel;
    private JLabel dateLabel;
    private JTextArea transactionArea;

    public HomePage(String username) {
        this.username = username;
        
        setTitle("Digital Banking System - Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPanel.setBackground(new Color(240, 242, 245));
        
        // Left panel - Account Summary
        JPanel leftPanel = createAccountPanel();
        contentPanel.add(leftPanel);
        
        // Right panel - Quick Actions and Transactions
        JPanel rightPanel = createQuickActionsPanel();
        contentPanel.add(rightPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Load initial data
        loadAccountData();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 53, 147));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Date label
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        updateDate();
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(255, 71, 87));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        // Add components to header
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setBackground(new Color(40, 53, 147));
        leftHeader.add(welcomeLabel);
        
        JPanel centerHeader = new JPanel();
        centerHeader.setBackground(new Color(40, 53, 147));
        centerHeader.add(dateLabel);
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(new Color(40, 53, 147));
        rightHeader.add(logoutButton);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(centerHeader, BorderLayout.CENTER);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createAccountPanel() {
        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)));
        
        // Account summary title
        JLabel summaryTitle = new JLabel("Account Summary");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Balance panel
        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel balanceTitle = new JLabel("Current Balance");
        balanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        balanceLabel = new JLabel("₹0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceLabel.setForeground(new Color(40, 53, 147));
        
        balancePanel.add(balanceTitle, BorderLayout.NORTH);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);
        
        // Account details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        detailsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel accountNoLabel = new JLabel("Account Number: " + getAccountNumber());
        accountNoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel accountTypeLabel = new JLabel("Account Type: Savings");
        accountTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel branchLabel = new JLabel("Branch: Main Branch");
        branchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel ifscLabel = new JLabel("IFSC Code: DBS123456");
        ifscLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        detailsPanel.add(accountNoLabel);
        detailsPanel.add(accountTypeLabel);
        detailsPanel.add(branchLabel);
        detailsPanel.add(ifscLabel);
        
        // Add components to account panel
        accountPanel.add(summaryTitle, BorderLayout.NORTH);
        accountPanel.add(balancePanel, BorderLayout.CENTER);
        accountPanel.add(detailsPanel, BorderLayout.SOUTH);
        
        return accountPanel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)));
        
        // Quick actions title
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionsTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Transfer Money button
        JButton transferButton = createActionButton("Transfer Money", new Color(66, 165, 245));
        transferButton.addActionListener(e -> showTransferDialog());
        
        // Deposit button
        JButton depositButton = createActionButton("Deposit", new Color(102, 187, 106));
        depositButton.addActionListener(e -> showDepositDialog());
        
        // Withdraw button
        JButton withdrawButton = createActionButton("Withdraw", new Color(239, 154, 154));
        withdrawButton.addActionListener(e -> showWithdrawDialog());
        
        // Create Account button
        JButton createAccountButton = createActionButton("Create Account", new Color(171, 71, 188));
        createAccountButton.addActionListener(e -> showCreateAccountDialog());
        
        buttonsPanel.add(transferButton);
        buttonsPanel.add(depositButton);
        buttonsPanel.add(withdrawButton);
        buttonsPanel.add(createAccountButton);
        
        // Recent transactions title
        JLabel transactionsTitle = new JLabel("Recent Transactions");
        transactionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        transactionsTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Transactions area
        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionArea.setBackground(new Color(248, 248, 248));
        transactionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(10, 10, 10, 10)));
        
        JScrollPane scrollPane = new JScrollPane(transactionArea);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        
        // Add components to actions panel
        actionsPanel.add(actionsTitle, BorderLayout.NORTH);
        actionsPanel.add(buttonsPanel, BorderLayout.CENTER);
        actionsPanel.add(transactionsTitle, BorderLayout.PAGE_END);
        actionsPanel.add(scrollPane, BorderLayout.SOUTH);
        
        return actionsPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(240, 242, 245));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel footerLabel = new JLabel("Digital Banking System © 2023 - All Rights Reserved");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateLabel.setText(sdf.format(new Date()));
    }
    
    private String getAccountNumber() {
        try {
            Account account = AccountDAO.getAccountByUsername(username);
            if (account != null) {
                return account.getAccountNumber();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not available";
    }
    
    private void loadAccountData() {
        try {
            Account account = AccountDAO.getAccountByUsername(username);
            if (account != null) {
                balanceLabel.setText("₹" + String.format("%,.2f", account.getBalance()));
                
                // Load transactions
                StringBuilder transactions = new StringBuilder();
                for (Transaction t : TransactionDAO.getRecentTransactions(account.getAccountNumber(), 5)) {
                    transactions.append(String.format("%-12s %-20s ₹%,10.2f %-15s\n",
                            t.getTransactionDate(),
                            t.getDescription(),
                            t.getAmount(),
                            t.getType()));
                }
                transactionArea.setText(transactions.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading account data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showTransferDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField toAccountField = new JTextField();
        JTextField amountField = new JTextField();
        JTextArea descriptionArea = new JTextArea(2, 20);
        
        panel.add(new JLabel("To Account Number:"));
        panel.add(toAccountField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer Money",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String toAccount = toAccountField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionArea.getText();
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive",
                            "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Get current account
                Account fromAccount = AccountDAO.getAccountByUsername(username);
                
                // Check if sufficient balance
                if (fromAccount.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance",
                            "Transfer Failed", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Perform transfer
                boolean success = AccountDAO.transferMoney(
                        fromAccount.getAccountNumber(),
                        toAccount,
                        amount,
                        description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Transfer successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAccountData(); // Refresh account info
                } else {
                    JOptionPane.showMessageDialog(this, "Transfer failed. Account not found.",
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showDepositDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField();
        JTextArea descriptionArea = new JTextArea(2, 20);
        
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Deposit Money",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionArea.getText();
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive",
                            "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Get current account
                Account account = AccountDAO.getAccountByUsername(username);
                
                // Perform deposit
                boolean success = AccountDAO.depositMoney(
                        account.getAccountNumber(),
                        amount,
                        description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Deposit successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAccountData(); // Refresh account info
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed",
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showWithdrawDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField();
        JTextArea descriptionArea = new JTextArea(2, 20);
        
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Withdraw Money",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionArea.getText();
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive",
                            "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Get current account
                Account account = AccountDAO.getAccountByUsername(username);
                
                // Check if sufficient balance
                if (account.getBalance() < amount) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance",
                            "Withdrawal Failed", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Perform withdrawal
                boolean success = AccountDAO.withdrawMoney(
                        account.getAccountNumber(),
                        amount,
                        description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAccountData(); // Refresh account info
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed",
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showCreateAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField fullNameField = new JTextField();
        JTextField initialDepositField = new JTextField();
        JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"Savings", "Current"});
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Initial Deposit:"));
        panel.add(initialDepositField);
        panel.add(new JLabel("Account Type:"));
        panel.add(accountTypeCombo);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Create New Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String fullName = fullNameField.getText();
                double initialDeposit = Double.parseDouble(initialDepositField.getText());
                String accountType = (String) accountTypeCombo.getSelectedItem();
                String phone = phoneField.getText();
                String email = emailField.getText();
                
                if (initialDeposit < 0) {
                    JOptionPane.showMessageDialog(this, "Initial deposit cannot be negative",
                            "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields",
                            "Incomplete Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Create new account
                boolean success = AccountDAO.createAccount(
                        username,
                        fullName,
                        accountType,
                        initialDeposit,
                        phone,
                        email);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Account created successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAccountData(); // Refresh account info
                } else {
                    JOptionPane.showMessageDialog(this, "Account creation failed",
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}