package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class User {
    Scanner scanner;
    Connection connection;

    public User(Scanner scanner, Connection connection) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Please Enter Email: ");
        String register_email = scanner.nextLine();
        if (!user_existed(register_email)) {
            String register_query = "INSERT INTO user(full_name,email,password) VALUES (?,?,?)";
            System.out.print("Enter your full name: ");
            String Full_name = scanner.nextLine();
            System.out.println();
            System.out.print("Enter your password: ");
            String Password = scanner.nextLine();
            System.out.println();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(register_query);
                preparedStatement.setString(1, Full_name);
                preparedStatement.setString(2, register_email);
                preparedStatement.setString(3, Password);
                int roweffected = preparedStatement.executeUpdate();
                if (roweffected > 0) {
                    System.out.println("Successfully Registered");
                } else {
                    System.out.println("Registration Failed. Please try again!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User Already existed for this email.");
            return;
        }
    }

    public String login() {
        scanner.nextLine();

        System.out.print("Enter your email: ");
        String Email = scanner.nextLine();
        System.out.println();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println();
        String Login_Query = "SELECT * FROM user WHERE email=? AND password=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Login_Query);
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Successfully Logged in...");
                return Email;
            } else {
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean user_existed(String register_email) {
        try {
            String sql_query = "SELECT * FROM user WHERE email= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, register_email);
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
