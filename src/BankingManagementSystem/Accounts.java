package BankingManagementSystem;

import java.util.Scanner;
import java.sql.*;

public class Accounts {

    private Scanner scanner;
    private Connection connection;

    public Accounts(Scanner scanner, Connection connection) {
        this.connection = connection;
        this.scanner = scanner;

    }

    public long open_accounts(String Email) {
        if (!account_exist(Email)) {
            String open_account_query = "INSERT INTO accounts(account_number, full_name,email,balance,sequrity_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            System.out.println();
            String user_full_name = scanner.nextLine();
            System.out.print("Email: ");
            System.out.println();
            String user_email = scanner.nextLine();
            System.out.print("Enter Balance to credit: ");
            System.out.println();
            double user_balance = scanner.nextDouble();

            System.out.print("Set Sequrity Pin: ");
            System.out.println();
            String user_pin = scanner.nextLine();
            try {
                long user_account_number = generate_account_number();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, user_account_number);
                preparedStatement.setString(2, user_full_name);
                preparedStatement.setString(3, user_email);
                preparedStatement.setDouble(4, user_balance);
                preparedStatement.setString(5, user_pin);
                int rows_effected = preparedStatement.executeUpdate();
                if (rows_effected > 0) {
                    System.out.println("Congratulations! Registration successfull");
                    System.out.println("Your account number is: " + user_account_number);
                } else {
                    throw new RuntimeException("Ragistration failed, Please Try again!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exists.");
    }

    public long generate_account_number() {
        long USER_ACCOUNT_NUMBER;
        try {
            String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                USER_ACCOUNT_NUMBER = resultSet.getLong("account_number");
                return USER_ACCOUNT_NUMBER+1;
            }
            else {
                return 10000100;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;

    }

    public long getAccount_number(String Email) {
        String getAccount_number_query = "SELECT account_number FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAccount_number_query);
            preparedStatement.setString(1, Email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account number does not exist, please open first.");

    }

    public boolean account_exist(String Email) {

        String account_exist_query = "SELECT * FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(account_exist_query);
            preparedStatement.setString(1, Email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;

            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}