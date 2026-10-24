package dao;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    boolean createAccount(Account account) throws SQLException;
    Account findAccount(long accountNumber) throws SQLException;
    boolean updateBalance(long accountNumber, BigDecimal balance) throws SQLException;
    List<Account> findAllAccounts() throws SQLException;
    BigDecimal changeBalance(long accountNumber,BigDecimal amount,boolean deposit) throws SQLException, AccountNotFoundException,
            InvalidAmountException, InsufficientBalanceException;
}
