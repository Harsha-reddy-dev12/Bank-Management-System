package model;

import java.math.BigDecimal;

public class Account {
    private long accountNumber;
    private String accountHolder;
    private String phoneNumber;
    private BigDecimal balance;

    //Constructor


    public Account(long accountNumber, String accountHolder, String phoneNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    //Getter and setter methods for variables
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public void displayAccountDetails(){
        System.out.println("Account Number: "+accountNumber);
        System.out.println("Account Holder: "+accountHolder);
        System.out.println("Phone Number: "+phoneNumber);
        System.out.println("Balance: "+balance);
    }

}