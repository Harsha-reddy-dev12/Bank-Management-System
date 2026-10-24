package dao;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO{
    @Override
    public boolean createAccount(Account account) throws SQLException{
         String sql="INSERT INTO accounts(account_number,account_holder,phone_number,balance)VALUES(?,?,?,?)";
         try(Connection connection= DBConnection.getConnection();
             PreparedStatement statement=connection.prepareStatement(sql)){
             statement.setLong(1,account.getAccountNumber());
             statement.setString(2, account.getAccountHolder());
             statement.setString(3, account.getPhoneNumber());
             statement.setBigDecimal(4,account.getBalance());
             int rowsInserted=statement.executeUpdate();
             return rowsInserted==1;

         }
    }
    @Override
    public Account findAccount(long accountNumber) throws SQLException{
        String sql="select account_number,account_holder,phone_number,balance from accounts where account_number=?";
        try(Connection connection=DBConnection.getConnection();
                PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1,accountNumber);
            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    long number=resultSet.getLong("account_number");
                    String holder=resultSet.getString("account_holder");
                    String phone=resultSet.getString("phone_number");
                    BigDecimal balance=resultSet.getBigDecimal("balance");
                    return new Account(number,holder,phone,balance);
                }
            }
        }

        return null;
    }

    @Override
    public boolean updateBalance(long accountNumber, BigDecimal balance) throws SQLException{
        String sql="update accounts set balance=? where account_number=?";
        try(Connection connection=DBConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setBigDecimal(1,balance);
            statement.setLong(2,accountNumber);
            return statement.executeUpdate()==1;
        }

    }

    @Override
    public List<Account> findAllAccounts() throws SQLException{
            List<Account> accounts=new ArrayList<>();
            String sql="select account_number,account_holder,phone_number,balance from accounts";
            try(Connection connection=DBConnection.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
                ResultSet resultSet= statement.executeQuery();
                while(resultSet.next()){
                    long number= resultSet.getLong("account_number");
                    String holder=resultSet.getString("account_holder");
                    String phone=resultSet.getString("phone_number");
                    BigDecimal balance=resultSet.getBigDecimal("balance");
                    Account account=new Account(number,holder,phone,balance);
                    accounts.add(account);
                }
            }
             return accounts;
    }
    @Override
    public BigDecimal changeBalance(
            long accountNumber,
            BigDecimal amount,
            boolean deposit
    ) throws SQLException, AccountNotFoundException,
            InvalidAmountException, InsufficientBalanceException {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        String selectSql = """
            SELECT balance
            FROM accounts
            WHERE account_number = ?
            FOR UPDATE
            """;
        String updateSql = """
            UPDATE accounts
            SET balance = ?
            WHERE account_number = ?
            """;

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                BigDecimal currentBalance;

                try (PreparedStatement selectStatement =
                             connection.prepareStatement(selectSql)) {

                    selectStatement.setLong(1, accountNumber);

                    try (ResultSet resultSet = selectStatement.executeQuery()) {

                        if (!resultSet.next()) {
                            throw new AccountNotFoundException("Account not found");
                        }

                        currentBalance = resultSet.getBigDecimal("balance");
                    }
                }

                BigDecimal newBalance;

                if (deposit) {
                    newBalance = currentBalance.add(amount);
                } else {

                    if (amount.compareTo(currentBalance) > 0) {
                        throw new InsufficientBalanceException(
                                "Insufficient balance"
                        );
                    }

                    newBalance = currentBalance.subtract(amount);
                }

                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateSql)) {

                    updateStatement.setBigDecimal(1, newBalance);
                    updateStatement.setLong(2, accountNumber);

                    if (updateStatement.executeUpdate() != 1) {
                        throw new SQLException("Balance update failed");
                    }
                }

                connection.commit();

                return newBalance;

            } catch (SQLException | AccountNotFoundException | InsufficientBalanceException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }

}


