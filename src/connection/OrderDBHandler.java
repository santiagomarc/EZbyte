package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import service.Cart;
import service.UserAccount;

public class OrderDBHandler {
    public static void updateBalanceInDB(Connection connection, int userId, float newBalance) {
        String sql = "UPDATE useraccount SET Balance = ? WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setFloat(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveOrderHistoryToDB(Connection connection, UserAccount user, Cart cart) {
        String sql = "INSERT INTO order_nutrition_history (UserID, OrderDate, TotalCalories, TotalCarbs, " +
                     "TotalProtein, TotalFat, TotalFiber, TotalSodium, TotalSugar, TotalPrice) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());
            pstmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setInt(3, cart.calculateTotalCalories());
            pstmt.setInt(4, cart.calculateTotalCarbs());
            pstmt.setInt(5, cart.calculateTotalProtein());
            pstmt.setInt(6, cart.calculateTotalFat());
            pstmt.setInt(7, cart.calculateTotalFiber());
            pstmt.setInt(8, cart.calculateTotalSodium());
            pstmt.setInt(9, cart.calculateTotalSugar());
            pstmt.setFloat(10, cart.calculateTotalCost());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving order nutrition history: " + e.getMessage());
        }
    }

    public static void displayUserNutritionHistory(Connection connection, int userId) {
        String sql = "SELECT OrderID, OrderDate, TotalCalories, TotalCarbs, TotalProtein, TotalFat, " +
                     "TotalFiber, TotalSodium, TotalSugar, TotalPrice FROM order_nutrition_history WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("\nNo nutrition history found for this user.");
                    return;
                }
                System.out.println("\n-------------------------------------- Your Order Nutritional History -----------------------------------------------");
                System.out.printf("%-10s | %-21s | %-8s | %-4s | %-4s | %-4s | %-4s | %-4s | %-6s | %-6s%n",
                                  "Order ID", "Order Date", "Price", "Cal", "Carb", "Prot", "Fib", "Fat", "Sod", "Sug");
                System.out.println("----------------------------------------------------------------------------------------------------------------------");
                do {
                    int orderId = rs.getInt("OrderID");
                    Timestamp orderDate = rs.getTimestamp("OrderDate");
                    float totalPrice = rs.getFloat("TotalPrice");
                    int totalCalories = rs.getInt("TotalCalories");
                    int totalCarbs = rs.getInt("TotalCarbs");
                    int totalProtein = rs.getInt("TotalProtein");
                    int totalFat = rs.getInt("TotalFat");
                    int totalFiber = rs.getInt("TotalFiber");
                    int totalSodium = rs.getInt("TotalSodium");
                    int totalSugar = rs.getInt("TotalSugar");
                    System.out.printf("%-10d | %-20s | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-4dmg | %-2dg%n", 
                                      orderId, orderDate.toString(), totalPrice, totalCalories, totalCarbs, totalProtein, 
                                      totalFat, totalFiber, totalSodium, totalSugar);
                } while (rs.next());
                System.out.println("----------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
