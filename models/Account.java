package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Account {
    private String accountNumber;
    private String username;
    private String fullName;
    private String accountType;
    private double balance;
    private String phone;
    private String email;
    private Date createdAt;

    public Account(String accountNumber, String username, String fullName, String accountType, 
                  double balance, String phone, String email, Date createdAt) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.fullName = fullName;
        this.accountType = accountType;
        this.balance = balance;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Account(String string, String string2, String string3, String string4, double double1, String string5,
			String string6, Timestamp timestamp) {
		// TODO Auto-generated constructor stub
	}

	public Account() {
		// TODO Auto-generated constructor stub
	}

	// Getters
    public String getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Date getCreatedAt() { return createdAt; }

	public void setId(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setAccountNumber(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setAccountType(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setBalance(double double1) {
		// TODO Auto-generated method stub
		
	}

	public void setCreatedAt(Timestamp timestamp) {
		// TODO Auto-generated method stub
		
	}
}