package usermanager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import main.Main;
import service.UserAccount;

public class UserManager {
    private Connection connection;
    Scanner scanner = Main.scanner;

    public UserManager() {
    }

    public Connection getConnection() {
         return connection; 
    }

    public void setConnection(Connection connection) { 
        this.connection = connection; 
    }

    public int login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String sql = "SELECT UserID, Password FROM useraccount WHERE Username = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("Password");
                    int userId = rs.getInt("UserID");
    
                    if (storedPassword.equals(password)) {
                        return userId; 
                    } else {
                        System.out.println("\033[31m\n\t\t\t      Incorrect password. Please try again!\033[0m");
                    }
                } else {
                    System.out.println("\033[31m\n\t\t\t   Username does not exist. Please try again!\033[0m");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return -1; 
    }
    
    public UserAccount getUserAccountById(int userId) {
        String sql = "SELECT * FROM useraccount WHERE UserID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
    
            if (resultSet.next()) {
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String address = resultSet.getString("Address");
                float balance = resultSet.getFloat("Balance"); 
    
                return new UserAccount(username, userId, password, phoneNumber, address, balance);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null; 
    }

    public void register() {
        String username = getUsername();
        if (username == null) return;

        String password = getPassword();
        if (password == null) return;

        String phoneNumber = getPhoneNumber();
        if (phoneNumber == null) return;

        String address = getAddress();
        if (address == null) return;

        if (registerUser(username, password, phoneNumber, address)) {
            System.out.println("\033[32m\n\t\t          User " + username + " registered successfully!\033[0m");
        }
    }
    private String getUsername() {
        System.out.print("Enter a username (at least 4 characters): ");
        String username = scanner.nextLine();
        if (username.length() < 4) {
            System.out.println("\033[31mUsername must be at least 4 characters long!\033[0m");
            return null;
        }
        if (isUsernameTaken(username)) {
            System.out.println("\033[31mUsername already exists!\033[0m");
            return null;
        }
        return username;
    }

    private String getPassword() {
        System.out.print("Enter a password (at least 8 characters): ");
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println("\033[31mPassword must be at least 8 characters long!\033[0m");
            return null;
        }
        return password;
    }

    private String getPhoneNumber() {
        System.out.print("Enter your phone number (exactly 11 digits): ");
        String phoneNumber = scanner.nextLine();
        if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
            System.out.println("\033[31mPhone number must be exactly 11 digits!\033[0m");
            return null;
        }
        return phoneNumber;
    }

    private String getAddress() {
        System.out.print("Enter your address: ");
        return scanner.nextLine();
    }

    
    public boolean isUsernameTaken(String username) {
        String checkUsernameSql = "SELECT COUNT(*) FROM useraccount WHERE Username = ?";
        try (PreparedStatement checkStmt = getConnection().prepareStatement(checkUsernameSql)) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking username: " + e.getMessage());
            return true; 
        }
    }
    
    public boolean registerUser(String username, String password, String phoneNumber, String address) {
        String insertSql = "INSERT INTO useraccount (Username, Password, PhoneNumber, Address, Balance) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(insertSql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, address);
            stmt.setBigDecimal(5, new BigDecimal("0.00")); 
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database error while registering user: " + e.getMessage());
        }
        return false;
    }
}
