package main;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.MenuItem;
import service.*;
import usermanager.UserManager;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            UserManager userManager = new UserManager();
            userManager.setConnection(connection);
            while (true) {
                System.out.println("\n\t\t    ==============================================================");
                System.out.println("\033[33m\t\t               Welcome to the EZBYTE Food Ordering System!\033[0m");
                System.out.println("\t\t    ==============================================================\n");
                System.out.println("1. Register User");
                System.out.println("2. Log in Ezbyte Account");
                System.out.println("3. Display Menu");
                System.out.println("4. Exit Ezbyte\n");
                int choice;
                System.out.print("Enter the number of your choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                } catch (InputMismatchException e) {
                    System.out.println("\033[31mError: Please enter a valid number (1-4).\033[0m");
                    scanner.nextLine(); 
                    continue;
                }

                switch (choice) {
                    case 1 -> {
                        userManager.register();
                    }
                    case 2 -> {
                        int userId = userManager.login();
                        if (userId != -1) {
                            UserAccount userAccount = userManager.getUserAccountById(userId);
                            if (userAccount != null) {
                                Cart userCart = new Cart(connection, userAccount);
                                userCart.loadCart();
                                userCart.setConnection(connection);

                                userAccount.setCart(userCart);
                                userAccount.setConnection(connection);

                                Order userOrder = new Order(userAccount, userCart);
                                userAccount.setOrder(userOrder); 
                                userOrder.setConnection(connection);

                                userAccount.userMenu(userId);
                            } else {
                                System.out.println("Error retrieving user account.");
                            }
                        }
                    }
                    case 3 -> MenuItem.displayMenu(connection); 
                    case 4 -> {
                        System.out.println("        ===========================================================================================");
                        System.out.println("        ||                                                                                       ||");
                        System.out.println("        ||                   _____ _                 _     __   __          _                    ||");
                        System.out.println("        ||                  |_   _| |__   __ _ _ __ | | __ \\ \\ / /__  _   _| |                   ||");
                        System.out.println("        ||                    | | | '_ \\ / _` | '_ \\| |/ /  \\ V / _ \\| | | | |                   ||");
                        System.out.println("        ||                    | | | | | | (_| | | | |   <    | | (_) | |_| |_|                   ||");
                        System.out.println("        ||                    |_| |_| |_|\\__,_|_| |_|_|\\_\\   |_|\\___/ \\__,_(_)                   ||");
                        System.out.println("        ||                                                                                       ||");
                        System.out.println("        ||                       for using the EZByte Food Ordering System!                      ||");
                        System.out.println("        ||                 -----------------------------------------------------                 ||");
                        System.out.println("        ||                                                                                       ||");
                        System.out.println("        ||                                                                                       ||");
                        System.out.println("        ===========================================================================================");                    
                        return;
                    }
                    default -> {
                        System.out.println("\033[31m\n\t\t\t            Please input a valid option\033[0m");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    scanner.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
