package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import service.AccountService;
import dao.TransactionDAO;
import model.Transaction;
import java.sql.Timestamp;

public class TransactionFrame extends JFrame {
    private JTextField amountField;

    public TransactionFrame() {
        setTitle("Digital Banking - Transaction");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performTransaction("deposit");
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performTransaction("withdraw");
            }
        });

        add(amountLabel);
        add(amountField);
        add(depositButton);
        add(withdrawButton);

        setVisible(true);
    }

    private void performTransaction(String type) {
        String amountText = amountField.getText();
        double amount = 0;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // For simplicity, assume userId = 1 (replace with actual logged-in user's ID)
        int userId = 1; // You should get this from the logged-in user session

        // Record transaction
        Transaction transaction = new Transaction(0, userId, type, amount, new Timestamp(System.currentTimeMillis()));
        TransactionDAO transactionDAO = new TransactionDAO();
        boolean success = transactionDAO.insertTransaction(transaction);

        if (success) {
            JOptionPane.showMessageDialog(this, "Transaction Successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Transaction Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
