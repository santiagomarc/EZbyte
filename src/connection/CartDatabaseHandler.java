package connection;

import java.sql.*;
import java.util.List;
import model.*;

public class CartDatabaseHandler {

    public static void saveCartToDatabase(List<CartItem> usercart, int userId, Connection connection) {
        if (usercart.isEmpty()) {
            return;
        }
        try {
            String updateSql = "UPDATE usercart SET Quantity = ? WHERE UserID = ? AND ItemID = ? AND CustomizationDetails = ?";
            String insertSql = "INSERT INTO usercart (UserID, ItemID, Quantity, CustomizationDetails, Calories, Carbs, Protein, Fiber, Fat, Sodium, Sugar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String deleteSql = "DELETE FROM usercart WHERE UserID = ? AND ItemID = ? AND CustomizationDetails = ?";

            for (CartItem cartItem : usercart) {
                if (cartItem.isMarkedForDeletion()) {
                    try (PreparedStatement deletePstmt = connection.prepareStatement(deleteSql)) {
                        deletePstmt.setInt(1, userId);
                        deletePstmt.setInt(2, cartItem.getMenuItem().getId());
                        deletePstmt.setString(3, cartItem.getCustomization());
                        deletePstmt.executeUpdate();
                        cartItem.resetDeletionFlag();
                    }    
                } else {
                    try (PreparedStatement updatePstmt = connection.prepareStatement(updateSql)) {
                        updatePstmt.setInt(1, cartItem.getQuantity());
                        updatePstmt.setInt(2, userId);
                        updatePstmt.setInt(3, cartItem.getMenuItem().getId());
                        updatePstmt.setString(4, cartItem.getCustomization());
                        int rowsUpdated = updatePstmt.executeUpdate();

                        if (rowsUpdated == 0) {
                            try (PreparedStatement insertPstmt = connection.prepareStatement(insertSql)) {
                                insertPstmt.setInt(1, userId);
                                insertPstmt.setInt(2, cartItem.getMenuItem().getId());
                                insertPstmt.setInt(3, cartItem.getQuantity());
                                insertPstmt.setString(4, cartItem.getCustomization());
                                insertPstmt.setInt(5, cartItem.getMenuItem().getCalories());
                                insertPstmt.setInt(6, cartItem.getMenuItem().getCarbs());
                                insertPstmt.setInt(7, cartItem.getMenuItem().getProtein());
                                insertPstmt.setInt(8, cartItem.getMenuItem().getFiber());
                                insertPstmt.setInt(9, cartItem.getMenuItem().getFat());
                                insertPstmt.setInt(10, cartItem.getMenuItem().getSodium());
                                insertPstmt.setInt(11, cartItem.getMenuItem().getSugar());
                                insertPstmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadCartFromDatabase(List<CartItem> usercart, int userId, Connection connection) {
        try {
            String sql = "SELECT ItemID, Quantity, CustomizationDetails, Calories, Carbs, Protein, Fiber, Fat, Sodium, Sugar FROM usercart WHERE UserID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                usercart.clear();

                while (rs.next()) {
                    int itemId = rs.getInt("ItemID");
                    int quantity = rs.getInt("Quantity");
                    String customizationDetails = rs.getString("CustomizationDetails");
                    int calories = rs.getInt("Calories");
                    int carbs = rs.getInt("Carbs");
                    int protein = rs.getInt("Protein");
                    int fiber = rs.getInt("Fiber");
                    int fat = rs.getInt("Fat");
                    int sodium = rs.getInt("Sodium");
                    int sugar = rs.getInt("Sugar");

                    MenuItem originalItem = getMenuItemById(connection, itemId);

                    if (originalItem != null) {
                        MenuItem customizedItem = switch (originalItem.getCategory()) {
                            case "Main Course" -> new MainCourse(itemId, originalItem.getName(), originalItem.getCategory(), originalItem.getPrice(),
                                    calories, carbs, protein, fiber, fat, sodium, sugar);
                            case "Sandwiches" -> new Sandwich(itemId, originalItem.getName(), originalItem.getCategory(), originalItem.getPrice(),
                                    calories, carbs, protein, fiber, fat, sodium, sugar);
                            case "Salads" -> new Salad(itemId, originalItem.getName(), originalItem.getCategory(), originalItem.getPrice(),
                                    calories, carbs, protein, fiber, fat, sodium, sugar);
                            case "Desserts" -> new Dessert(itemId, originalItem.getName(), originalItem.getCategory(), originalItem.getPrice(),
                                    calories, carbs, protein, fiber, fat, sodium, sugar);
                            case "Beverages" -> new Beverage(itemId, originalItem.getName(), originalItem.getCategory(), originalItem.getPrice(),
                                    calories, carbs, protein, fiber, fat, sodium, sugar);
                            default -> null;
                        };

                        if (customizedItem != null) {
                            CartItem cartItem = new CartItem(customizedItem, quantity, customizationDetails);
                            usercart.add(cartItem);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MenuItem getMenuItemById(Connection conn, int itemId) {
        MenuItem item = null;
        String sql = "SELECT ItemID, Category, Name, Price, Calories, Carbs, Protein, Fiber, Fat, Sodium, Sugar FROM MENUITEM WHERE ItemID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ItemID");
                String itemName = rs.getString("Name");
                String category = rs.getString("Category");
                float price = rs.getFloat("Price");
                int calories = rs.getInt("Calories");
                int carbs = rs.getInt("Carbs");
                int protein = rs.getInt("Protein");
                int fiber = rs.getInt("Fiber");
                int fat = rs.getInt("Fat");
                int sodium = rs.getInt("Sodium");
                int sugar = rs.getInt("Sugar");

                item = switch (category) {
                    case "Main Course" -> new MainCourse(id, itemName, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                    case "Sandwiches" -> new Sandwich(id, itemName, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                    case "Salads" -> new Salad(id, itemName, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                    case "Desserts" -> new Dessert(id, itemName, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                    case "Beverages" -> new Beverage(id, itemName, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                    default -> null;
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
}
