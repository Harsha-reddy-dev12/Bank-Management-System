package services;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import interfaces.Payment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import model.Account;

import exception.AccountNotFoundException;

public class BankService implements Payment {

    private AccountDAO accountDAO=new AccountDAOImpl();

    public void createAccount(
            long accountNumber,
            String accountHolder,
            String phoneNumber,
            BigDecimal initialBalance) {

        try {
            // Check whether the account already exists.
            if (accountDAO.findAccount(accountNumber) != null) {
                System.out.println("Account already exists");
                return;
            }

            // Validate account holder name.
            if (accountHolder == null || accountHolder.trim().isEmpty()
                    || !accountHolder.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid name. Please enter proper name");
                return;
            }

            // Validate phone number.
            if (phoneNumber == null
                    || !phoneNumber.matches("[6-9][0-9]{9}")) {
                System.out.println("Invalid phone number");
                return;
            }

            // Validate initial balance.
            if (initialBalance == null
                    || initialBalance.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Initial balance must be greater than zero");
                return;
            }

            Account account = new Account(
                    accountNumber, accountHolder, phoneNumber, initialBalance
            );

            // Save the account in MySQL.
            boolean result = accountDAO.createAccount(account);

            if (result) {
                System.out.println("Account created successfully");
            } else {
                System.out.println("Account creation failed");
            }

        } catch (SQLException e) {
            System.out.println("Database error while creating account.");
            e.printStackTrace();
        }
    }

    public Account findAccount(long accountNumber) {
        try {
            return accountDAO.findAccount(accountNumber);
        } catch (SQLException e) {
            System.out.println("Database error while finding account.");
            e.printStackTrace();
            return null;
        }
    }

    private Account getAccountOrThrow(long accountNumber)
            throws AccountNotFoundException {

        try {
            Account account = accountDAO.findAccount(accountNumber);

            if (account == null) {
                throw new AccountNotFoundException("Account not found");
            }

            return account;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database error while finding account.", e
            );
        }
    }

    //implementation of interface methods

    @Override
    public void deposit(long accountNumber, BigDecimal amount)
            throws AccountNotFoundException, InvalidAmountException {

        try {
            BigDecimal newBalance = accountDAO.changeBalance(
                    accountNumber, amount, true
            );

            System.out.println(amount + " deposited successfully");
            System.out.println("Available balance: " + newBalance);

        } catch (SQLException e) {
            System.out.println("Deposit failed due to a database error.");
            e.printStackTrace();

        } catch (InsufficientBalanceException e) {
            // A deposit should not cause an insufficient-balance error.
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void withdraw(long accountNumber, BigDecimal amount)
            throws AccountNotFoundException, InvalidAmountException,
            InsufficientBalanceException {

        try {
            BigDecimal newBalance = accountDAO.changeBalance(
                    accountNumber, amount, false
            );

            System.out.println(amount + " withdrawn successfully");
            System.out.println("Available balance: " + newBalance);

        } catch (SQLException e) {
            System.out.println("Withdrawal failed due to a database error.");
            e.printStackTrace();
        }
    }

    public void displayAllAccounts() {
        try {
            List<Account> accounts = accountDAO.findAllAccounts();

            if (accounts.isEmpty()) {
                System.out.println("No accounts found");
                return;
            }

            System.out.println("All Bank Accounts");

            for (Account account : accounts) {
                account.displayAccountDetails();
                System.out.println("----------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Database error while displaying accounts");
            e.printStackTrace();
        }
    }

    //check account details
    public void checkAccountDetails(long accountNumber)
            throws AccountNotFoundException {

        Account account = getAccountOrThrow(accountNumber);
        account.displayAccountDetails();
    }




}