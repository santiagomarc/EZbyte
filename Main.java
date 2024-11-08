import java.sql.Connection;
import java.sql.SQLException;
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
                System.out.println("1. Register User");
                System.out.println("2. Log in Ezbyte Account");
                System.out.println("3. Display Menu");
                System.out.println("4. Exit Ezbyte");

                int choice = scanner.nextInt();
                scanner.nextLine();  

                switch (choice) {
                    case 1:
                        System.out.println("Register a New User");

                        System.out.print("Enter a username (at least 4 characters): ");
                        String newUsername = scanner.nextLine();
                        if (newUsername.length() < 4) {
                            System.out.println("Error: Username must be at least 4 characters long.");
                            break;
                        }
                        if (userManager.isUsernameTaken(newUsername)) {
                            System.out.println("Error: Username already exists. Please choose a different username.");
                            break;
                        }
                        System.out.print("Enter a password (at least 8 characters): ");
                        String newPassword = scanner.nextLine();
                        if (newPassword.length() < 8) {
                            System.out.println("Error: Password must be at least 8 characters long.");
                            break;
                        }

                        System.out.print("Enter your phone number (exactly 11 digits): ");
                        String phoneNumber = scanner.nextLine();
                        if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
                            System.out.println("Error: Phone number must be exactly 11 digits.");
                            break;
                        }
                        if (phoneNumber.isEmpty()) {
                            System.out.println("Error: Phone number cannot be empty.");
                            break;
                        }

                        System.out.print("Enter your address: ");
                        String address = scanner.nextLine();
                        if (address.isEmpty()) {
                            System.out.println("Error: Address cannot be empty.");
                            break;
                        }
                        // para madetermine kung register success
                        boolean registrationSuccess = userManager.registerUser(newUsername, newPassword, phoneNumber, address);
                        if (registrationSuccess) {
                            System.out.println("Registration successful! You can now log in.");
                        } else {
                            System.out.println("Registration failed. Please try again.");
                        }
                    break;
                    case 2:
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        int userId = userManager.verifyLogin(username, password);

                        if (userId != -1) {
                            System.out.println("Login successful!");
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
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                        break;
                        case 3:

                        System.out.println("Choose a category to display:");
                        System.out.println("1. Main Courses");
                        System.out.println("2. Sandwiches");
                        System.out.println("3. Salads");
                        System.out.println("4. Desserts");
                        System.out.println("5. Beverages");
                    
                        System.out.print("Enter the number of your choice: ");
                        int categoryChoice = scanner.nextInt();
                        scanner.nextLine(); 
                    
                        MenuItem.displayMenu(connection, categoryChoice); 
                        break;                    
                    case 4:
                        System.out.println("Exiting the system. Thank you for using Ezbyte!");
                        return;
                    default:
                        System.out.println("Please input a valid option");
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
