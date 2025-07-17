package BankingManagementSystem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class AccountsManager {
    private Scanner scanner;
    private Connection connection;

    public AccountsManager(Scanner scanner, Connection connection) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void debit_money(long debit_account_number) throws SQLException {
        System.out.print("Enter amount to witdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter your pin: ");
        String pin = scanner.nextLine();
        System.out.println();
        try {
            connection.setAutoCommit(false);
            if (debit_account_number != 0) {
                String credential_authentication = "SELECT * FROM accounts WHERE account_number=? AND security_pin=?";
                PreparedStatement preparedStatement = connection.prepareStatement(credential_authentication);
                preparedStatement.setLong(1, debit_account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double outstanding_balance = resultSet.getDouble("balance");
                    if (outstanding_balance >= amount) {
                        String debit_query = "UPDATE accounts SET balance= balance-? WHERE account_number=?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, debit_account_number);
                        int rowseffected = preparedStatement1.executeUpdate();
                        if (rowseffected > 0) {
                            System.out.println("Successfully debited " + amount + " to " + debit_account_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;

                        } else {
                            System.out.println("Debit failed! please try again.");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                } else {
                    throw new RuntimeException("Invalid Pin. Please Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void credit_money(long credit_account_number) throws SQLException {
        System.out.print("Enter amount to credit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter your pin: ");
        String pin = scanner.nextLine();
        System.out.println();
        try {
            connection.setAutoCommit(false);
            String credential_authentication = "SELECT * FROM accounts WHERE account_number=? AND security_pin=?";
            PreparedStatement preparedStatement = connection.prepareStatement(credential_authentication);
            preparedStatement.setLong(1, credit_account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String credit_query = "UPDATE accounts SET balance= balance+? and account_number=?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                preparedStatement1.setDouble(1, amount);
                preparedStatement1.setLong(2, credit_account_number);
                int rowseffected = preparedStatement1.executeUpdate();
                if (rowseffected > 0) {
                    System.out.println("Successfully credited " + amount + " to " + credit_account_number);
                    connection.commit();
                    connection.setAutoCommit(true);
                    return;
                } else {
                    System.out.println("Credit failed. Please try again.");
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } else {
                throw new RuntimeException("Invalid pin. please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);

    }

    public void transfer_money(long sender_account_number) throws SQLException {
        System.out.print("Enter reciever account number: ");
        long reciever_account_number = scanner.nextLong();
        System.out.println();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter pin: ");
        String pin = scanner.nextLine();
        System.out.println();
        try {
            connection.setAutoCommit(false);
            String reciever_authentication_query = "SELECT * FROM accounts WHERE account_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(reciever_authentication_query);
            preparedStatement.setLong(1, reciever_account_number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String sender_balance_query = "SELECT balance FROM accounts WHERE account_number=? AND security_pin=?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sender_balance_query);
                preparedStatement1.setLong(1, sender_account_number);
                preparedStatement1.setString(2, pin);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    double outstanding_balance = resultSet1.getDouble("balance");
                    if (outstanding_balance >= amount) {
                        String debit_query = "UPDATE accounts SET balance= balance-? WHERE account_number=?";
                        String credit_query="UPDATE accounts SET balance =balance+? WHERE account_number=?";
                        PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                        PreparedStatement creditpreparedStatement=connection.prepareStatement(credit_query);
                        debitpreparedStatement.setDouble(1,amount);
                        debitpreparedStatement.setLong(2,sender_account_number);
                        creditpreparedStatement.setDouble(1,amount);
                        creditpreparedStatement.setLong(2,reciever_account_number);
                        int rowseffected1=debitpreparedStatement.executeUpdate();
                        int rowseffected2=creditpreparedStatement.executeUpdate();
                        if(rowseffected1>0&&rowseffected2>0){
                            System.out.println("Successfully transferred "+amount+" to "+reciever_account_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transfer Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {

                        throw new RuntimeException("Insufficient balance.");
                    }
                } else {
                    throw new RuntimeException("Invalid Pin.");
                }
            } else {
                throw new RuntimeException("Account number does not exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String Security_pin = scanner.nextLine();
        System.out.println();
        String GetBalanceQuery = "SELECT balance FROM accounts WHERE account_number=? AND security_pin=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GetBalanceQuery);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, Security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double Outstanding_balance = resultSet.getDouble("balance");
                System.out.println("Your Outstanding Balance is: " + Outstanding_balance);
            } else {
                System.out.println("Invalid Pin. Try Again");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
