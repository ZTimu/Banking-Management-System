package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "21335756";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//jdbc driver loaded
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);// connection established
            Scanner scanner = new Scanner(System.in);
            Accounts accounts = new Accounts(connection, scanner);
            AccountsManager accountsManager = new AccountsManager(connection, scanner);
            User user = new User(connection, scanner);
            String Email;
            long account_number;
            while (true) {
                System.out.println("Welcome to MyBank \n");
                System.out.println("Choose an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println();
                System.out.println("Enter Choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
//                    user.register();
                        break;
                    case 2:
//                        Email = user.login(); need to be implemented
                        if(Email!=null) {
                            System.out.println("Successfully Logged in.");
                            if (!accounts.account_exist(Email)) {
                                System.out.println("Choose an option!");
                                System.out.println();
                                System.out.println("1. Create a new Bank Account");
                                System.out.println("2.Exit");
                                int loginchoice;
                                loginchoice = scanner.nextInt();
                                if (loginchoice == 1) {
                                    account_number = accounts.open_account(Email);
                                    System.out.println("Account opened successfully");
//                                   System.out.println("Your account number is...");
                                } else {
                                    System.out.println("See you again.");
                                    break;
                                }

                            }

                            account_number = accounts.getAccount_number(Email);
                            int accountOperationChoice;
                            System.out.println("Choose an option");
                            System.out.println();
                            System.out.println("1. Debit money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Log Out");
                            accountOperationChoice = scanner.nextInt();

                            switch (accountOperationChoice) {
                                case 1:
                                    accountsManager.debit_money(account_number);
                                    break;
                                case 2:
                                    accountsManager.credit_money(account_number);
                                    break;
                                case 3:
                                    accountsManager.transfer_money(account_number);
                                    break;
                                case 4:
                                    accountsManager.getBalance(account_number);
                                    break;
                                case 5:
                                    return;
                                default:
                                    System.out.println("Enter a valid Option");
                                    break;
                            }
                        }
                        else {
                            System.out.println("Incorrect Email. Please Try again.");
                        }
                        break;

                    case 3:
                        System.out.println("Thank you. Have a nice day!");
                        return;

                    default:
                        System.out.println("Enter a valid choice.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}