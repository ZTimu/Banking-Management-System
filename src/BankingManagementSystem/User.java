package BankingManagementSystem;

import java.sql.Connection;
import java.util.Scanner;

public class User {
    Scanner scanner;
    Connection connection;
    public User(Scanner scanner, Connection connection){
        this.scanner=scanner;
        this.connection=connection;
    }
}
