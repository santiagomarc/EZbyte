import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            UserManager userManager = new UserManager();
            userManager.setConnection(connection);

            while (true) {
                System.out.println("\n\t\t==============================================================");
                System.out.println("\t\t           Welcome to the EZBYTE Food Ordering System!");
                System.out.println("\t\t==============================================================\n");
                System.out.println("1. Register User");
                System.out.println("2. Log in Ezbyte Account");
                System.out.println("3. Display Menu");
                System.out.println("4. Exit Ezbyte\n");

                int choice;
                System.out.print("Enter the number of your choice: ");
                
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();  // Clear buffer
                } catch (InputMismatchException e) {
                    System.out.println("Error: Please enter a valid number (1-4).");
                    scanner.nextLine();  // Clear the invalid input
                    continue;
                }

                switch (choice) {
                    case 1 -> {
                        System.out.println("\n\t\t\t  -------------REGISTER NEW USER-------------\n");
                        System.out.print("Enter a username (at least 4 characters): ");
                        String newUsername = scanner.nextLine();
                        if (newUsername.length() < 4 ) {
                            System.out.println("\n\t\t\t  ===========================================");
                            System.out.println("\t\t\t  Username must be at least 4 characters long");
                            System.out.println("\t\t\t  ===========================================");
                            break;
                        }
                        if (userManager.isUsernameTaken(newUsername)) {
                            System.out.println("\n\t\t\t    ======================================================");
                            System.out.println("\t\t\t    Username already exists. Please choose a different one");
                            System.out.println("\t\t\t    ======================================================\n");
                            break;
                        }

                        System.out.print("Enter a password (at least 8 characters): ");
                        String newPassword = scanner.nextLine();
                        if (newPassword.length() < 8) {
                            System.out.println("\n\t\t\t  ===========================================");
                            System.out.println("\t\t\t  Password must be at least 8 characters long");
                            System.out.println("\t\t\t  ===========================================\n");
                            break;
                        }

                        System.out.print("Enter your phone number (exactly 11 digits): ");
                        String phoneNumber = scanner.nextLine();
                        if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
                            System.out.println("\n\t\t\t  ===========================================");
                            System.out.println("\t\t\t  Phone number must be exactly 11 digits long");
                            System.out.println("\t\t\t  ===========================================\n");
                            break;
                        }

                        System.out.print("Enter your address: ");
                        String address = scanner.nextLine();
                        if (address.isEmpty()) {
                            System.out.println("\n\t\t\t  ===========================================");
                            System.out.println("\t\t\t              Address cannot be empty");
                            System.out.println("\t\t\t  ===========================================\n");
                            break;
                        }

                        boolean registrationSuccess = userManager.registerUser(newUsername, newPassword, phoneNumber, address);
                        if (!registrationSuccess) {
                            System.out.println("\n\t\t\t\t-------------------------------");
                            System.out.println("\t\t\t\t       Registration Failed!");
                            System.out.println("\t\t\t\t-------------------------------\n");
                        }
                    }
                    
                    case 2 -> {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        int userId = userManager.verifyLogin(username, password);

                        if (userId != -1) {
                            UserAccount userAccount = userManager.getUserAccountById(userId);
                            if (userAccount != null) {
                                Cart userCart = new Cart(connection, userAccount);
                                userCart.loadCartFromDatabase();
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

                    case 3 -> MenuItem.displayMenu(connection); // Displays all categories at once

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
                        System.out.println("\n\t\t\t==============================================");
                        System.out.println("\t\t\t          Please input a valid option");
                        System.out.println("\t\t\t==============================================\n");                    
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
