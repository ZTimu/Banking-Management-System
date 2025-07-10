package BankingManagementSystem;

import java.sql.Connection;
import java.util.Scanner;

public class AccountsManager {
    private Scanner scanner;
    private Connection connection;
    public AccountsManager(Scanner scanner, Connection connection){
        this.scanner=scanner;
        this.connection=connection;
    }
}
