package main;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import model.Account;

import java.math.BigDecimal;

public class AccountDAOTest {
    public static void main(String[] args) throws java.sql.SQLException {
        AccountDAO accountDAO=new AccountDAOImpl();
        Account account=new Account(1001L,"Harsha Reddy","9876543210",new BigDecimal("50000.00"));
        boolean result=accountDAO.createAccount(account);
        if(result){
            System.out.println("Account created successfully");
        }
        else{
            System.out.println("Account creation failed");
        }
    }
}
