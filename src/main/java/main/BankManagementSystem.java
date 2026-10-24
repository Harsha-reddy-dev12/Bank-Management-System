package main;

import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import services.BankService;

import java.math.BigDecimal;
import java.util.Scanner;

public class BankManagementSystem {
    public static void main(String[] args)  {
        BankService bankService = new BankService();
        Scanner scanner=new Scanner(System.in);
        int choice=0;
        while(choice!=6){
            System.out.println("===BANK MANAGEMENT SYSTEM===");
            System.out.println("1.CREATE ACCOUNT");
            System.out.println("2.DEPOSIT MONEY");
            System.out.println("3.WITHDRAW MONEY");
            System.out.println("4.CHECK ACCOUNT DETAILS");
            System.out.println("5.DISPLAY ALL ACCOUNTS");
            System.out.println("6.EXIT");
            System.out.print("Enter your choice: ");
            try{
                choice=scanner.nextInt();
                switch(choice){
                    case 1:
                        System.out.print("Enter account Number: ");
                        long accountNumber=scanner.nextLong();
                        scanner.nextLine();
                        System.out.print("Enter account holder: ");
                        String accountHolder=scanner.nextLine();
                        if(accountHolder.trim().isEmpty() || !accountHolder.matches("[a-zA-Z ]+")){
                            System.out.println("Invalid name");
                            break;
                        }
                        System.out.print("Enter phone number: ");
                        String phoneNumber=scanner.nextLine();
                        if(!phoneNumber.matches("[0-9]{10}")||phoneNumber.charAt(0)<'6' || phoneNumber.charAt(0)>'9'){
                            System.out.println("Invalid phone number.Phone number must have 10 digits.Number must start with 6,7,8,9");
                            break;
                        }
                        System.out.print("Enter initial balance: ");
                        BigDecimal initialBalance=scanner.nextBigDecimal();
                        bankService.createAccount(accountNumber,accountHolder,phoneNumber,initialBalance);
                        break;

                    case 2:
                        System.out.print("Enter account Number: ");
                        accountNumber=scanner.nextLong();
                        if(bankService.findAccount(accountNumber)==null){
                            System.out.println("no bank account found");
                        }
                        else {
                            System.out.print("Enter the amount to deposit: ");
                            BigDecimal amount = scanner.nextBigDecimal();
                            try {
                                bankService.deposit(accountNumber, amount);
                            } catch (AccountNotFoundException | InvalidAmountException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Enter account Number: ");
                        accountNumber=scanner.nextLong();
                        if(bankService.findAccount(accountNumber)==null){
                            System.out.println("No bank account found");
                        }
                        else {
                            System.out.print("Enter the amount to withdraw: ");
                            BigDecimal amount = scanner.nextBigDecimal();
                            try {
                                bankService.withdraw(accountNumber, amount);
                            } catch (AccountNotFoundException | InvalidAmountException |
                                     InsufficientBalanceException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;
                    case 4:
                        System.out.print("enter account number: ");
                        accountNumber=scanner.nextLong();
                        try{
                            bankService.checkAccountDetails(accountNumber);
                        }
                        catch(AccountNotFoundException e){
                            System.out.println("Error: "+e.getMessage());
                        }
                        break;
                    case 5:
                        bankService.displayAllAccounts();
                        break;
                    case 6:
                        System.out.println("Thank you for using our banking system");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 to 6");

                }
            }
            catch(java.util.InputMismatchException e){
                System.out.println("Invalid input please enter the correct data type");
                scanner.nextLine();
            }
        }
    }
}