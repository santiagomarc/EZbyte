package service;
import connection.OrderDBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import main.Main;

public class UserAccount {
    private final String username;
    private final int userId;
    private final String password;
    private final String phoneNumber;
    private final String address;
    private float balance;
    private Cart cart; 
    private Order order;
    private Connection connection;

    public UserAccount(String username, int userId, String password, String phoneNumber, String address, float balance) {
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.balance = balance;
    }

    public String getUsername() { return username; }
    public int getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public float getBalance() { return balance; }
    public Cart getCart() { return cart; }
    public Connection getConnection() { return connection; }

    public void setBalance(float balance) { this.balance = balance; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setOrder(Order order) { this.order = order; }
    public void setConnection(Connection connection) { this.connection = connection; }
    
    Scanner scanner = Main.scanner;
    public void userMenu(int user_id) {
        cart.loadCart();
        while (true) {
            System.out.println("\n\t\t\t  ===================================================");
            System.out.println("\033[33m\t\t\t          Hello " + getUsername() + "!  -- Balance: P " + getBalance() + "\033[0m");
            System.out.println("\t\t\t  ===================================================\n");
            System.out.println("1. Add to Cart");
            System.out.println("2. Edit Your Cart");
            System.out.println("3. Top-up Ezbyte Wallet");
            System.out.println("4. View Cart and Proceed to Payment");
            System.out.println("5. View Order and Nutrition History");  
            System.out.println("6. Check Credentials");
            System.out.println("7. Log Out");
            System.out.print("\033[1;34mEnter the number of your choice: \033[0m");
    
            int choice;
            try {
                choice = Main.scanner.nextInt();  
                Main.scanner.nextLine();  
            } catch (InputMismatchException e) {
                System.out.println("\033[31m\t\t\t                Please input a valid option.\033[0m");
                Main.scanner.nextLine();  
                continue;
            }
    
            switch (choice) {
                case 1 -> cart.addItemToCart();
                case 2 -> cart.editItem();
                case 3 -> topUpWallet(getUserId());
                case 4 -> order.processOrder();
                case 5 -> OrderDBHandler.displayUserNutritionHistory(getConnection(), user_id);  
                case 6 -> checkCredentials();
                case 7 -> {
                    System.out.println("\n\t\t\t------------------------------------------------------");
                    System.out.println("\033[33m\t\t\t\t    User: '" + getUsername() + "' has logged out!\033[0m");
                    System.out.println("\t\t\t------------------------------------------------------\n");
                    return;
                }
                default -> {
                    System.out.println("\033[31m\t\t\t             Invalid selection! Please try again.\033[0m");
                }
            }
        }
    }
    
    public void topUpWallet(int userId) {
        try (
            PreparedStatement fetchBalanceStmt = getConnection().prepareStatement("SELECT Balance FROM useraccount WHERE UserID = ?");
            PreparedStatement updateBalanceStmt = getConnection().prepareStatement("UPDATE useraccount SET Balance = ? WHERE UserID = ?");
        ) {
            fetchBalanceStmt.setInt(1, userId);
            float currentBalance;
            try (ResultSet resultSet = fetchBalanceStmt.executeQuery()) {
                currentBalance = 0;
                if (resultSet.next()) {
                    currentBalance = resultSet.getFloat("Balance");
                } 
            }
            System.out.println("\n\t\t\t       =========================================");
            System.out.println("\033[33m\t\t\t                 Top-up Ezbyte Wallet\033[0m");
            System.out.println("\t\t\t       =========================================\n");
            System.out.print("Enter the amount to top up (in peso): P");
    
            float amount;
            try {
                amount = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                scanner.nextLine();
                throw new IllegalArgumentException("  Invalid input. Please enter a valid amount.");
            }
    
            if (amount > 0) {
                float newBalance = currentBalance + amount;
                updateBalanceStmt.setFloat(1, newBalance);
                updateBalanceStmt.setInt(2, userId);
                updateBalanceStmt.executeUpdate();
                setBalance(newBalance);
                System.out.printf("\n\t\t\t------------------------------------------------------\n");
                System.out.printf("\033[32m\t\t\t      Top-up successful. New balance: P %.2f\n\033[0m", newBalance);
                System.out.printf("\t\t\t------------------------------------------------------\n");
            } else {
                System.out.println("\n\t\t\t  =============================================");
                System.out.println("\033[31m\t\t\t  Invalid amount. Please enter a positive value\033[0m");
                System.out.println("\t\t\t  =============================================\n");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n\t\t\t  ==============================================");
            System.out.println("\t\t\t  " + e.getMessage());
            System.out.println("\t\t\t  ==============================================\n");
        } catch (SQLException e) {
            System.out.println("\n\t\t\t  ==============================================");
            System.out.println("\t\t\t  Database error: " + e.getMessage());
            System.out.println("\t\t\t  ==============================================\n");
        }
    }
    public void checkCredentials() {
        System.out.println("\033[32m\n\t\t\t------------------- User Information -------------------");
        System.out.println("\t\t\tUsername: " + getUsername());
        System.out.println("\t\t\tUserID: " + getUserId());
        System.out.println("\t\t\tPhone Number: " + getPhoneNumber());
        System.out.println("\t\t\tAddress: " + getAddress());
        System.out.printf("\t\t\tBalance: P %.2f\n", getBalance());
        System.out.println("\t\t\t-------------------------------------------------------\033[0m");
        if (!cart.isEmpty()) {  
            cart.displayCart();
        } else {
            System.out.println("\033[31m\t\t\t\t          User's Cart is empty!\033[0m");
        }
    }    
}
