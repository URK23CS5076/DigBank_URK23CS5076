package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountFrame extends JFrame {

    private JTextField nameField, ageField, addressField, mobileField, emailField;
    private JButton submitButton;

    public CreateAccountFrame() {
        setTitle("Create New Account - Global Trust Bank");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel titleLabel = new JLabel("Fill Your Personal Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 400, 30);
        add(titleLabel);

        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 80, 150, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 80, 200, 25);
        add(nameField);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(50, 130, 150, 25);
        add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(200, 130, 200, 25);
        add(ageField);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 180, 150, 25);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(200, 180, 200, 25);
        add(addressField);

        // Mobile Number
        JLabel mobileLabel = new JLabel("Mobile No:");
        mobileLabel.setBounds(50, 230, 150, 25);
        add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setBounds(200, 230, 200, 25);
        add(mobileField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 280, 150, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(200, 280, 200, 25);
        add(emailField);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBounds(150, 350, 200, 30);
        add(submitButton);

        // Submit button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect all details
                String name = nameField.getText();
                String age = ageField.getText();
                String address = addressField.getText();
                String mobile = mobileField.getText();
                String email = emailField.getText();

                // Very basic validation
                if (name.isEmpty() || age.isEmpty() || address.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateAccountFrame.this, 
                            "Please fill all the fields!", 
                            "Missing Details", 
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // In real project, save to database here
                    JOptionPane.showMessageDialog(CreateAccountFrame.this,
                            "Account Created Successfully!\n\nWelcome, " + name + "!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the window after success
                }
            }
        });
    }
}
