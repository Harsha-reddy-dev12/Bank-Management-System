package interfaces;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;

import exception.AccountNotFoundException;
import java.math.BigDecimal;

public interface Payment {
    void deposit(long accountNumber, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException;
    void withdraw(long accountNumber,BigDecimal amount) throws AccountNotFoundException,InvalidAmountException, InsufficientBalanceException;
}