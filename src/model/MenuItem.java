package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MenuItem {
    protected int id;
    protected String name;
    protected String category; 
    protected float price;
    protected int calories, carbs, protein, fiber, fat, sodium, sugar;

    public MenuItem(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        this.id = id;
        this.name = name;
        this.category = category; 
        this.price = price;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fiber = fiber;
        this.fat = fat;
        this.sodium = sodium;
        this.sugar = sugar;
    }


    public abstract String displayDetails();
    public abstract void customize();

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public float getPrice() { return price; }
    public int getCalories() { return calories; }
    public int getCarbs() { return carbs; }
    public int getProtein() { return protein; }
    public int getFiber() { return fiber; }
    public int getFat() { return fat; }
    public int getSodium() { return sodium; }
    public int getSugar() { return sugar; } 

    public static void displayMenu(Connection connection) { 
        String boldYellow = "\033[1m\033[33m";
        String reset = "\033[0m";
        String[] categories = {"Main Course", "Sandwiches", "Salads", "Desserts", "Beverages"};
        
        for (String category : categories) {
            List<MenuItem> menuItems = new ArrayList<>();
            String sql = "SELECT ItemID, Category, Name, Price, Calories, Carbs, Protein, Fiber, Fat, Sodium, Sugar FROM MENUITEM WHERE Category = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("ItemID");
                    String name = rs.getString("Name");
                    float price = rs.getFloat("Price");
                    int calories = rs.getInt("Calories");
                    int carbs = rs.getInt("Carbs");
                    int protein = rs.getInt("Protein");
                    int fiber = rs.getInt("Fiber");
                    int fat = rs.getInt("Fat");
                    int sodium = rs.getInt("Sodium");
                    int sugar = rs.getInt("Sugar");

                    MenuItem item = switch (category) {
                        case "Main Course" -> new MainCourse(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                        case "Sandwiches" -> new Sandwich(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                        case "Salads" -> new Salad(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                        case "Desserts" -> new Dessert(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                        case "Beverages" -> new Beverage(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                        default -> null;
                    };

                    if (item != null) {
                        menuItems.add(item);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving menu items: " + e.getMessage());
            }

            if (!menuItems.isEmpty()) {
                System.out.printf("%s%55s%s%n", boldYellow, category, reset);
                System.out.println("------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-47s | %-8s | %-4s | %-4s | %-4s | %-4s | %-4s | %-5s | %-4s%n",
                                "Item", "Price", "Cal", "Carb", "Prot", "Fib", "Fat", "Sod", "Sug");
                System.out.println("------------------------------------------------------------------------------------------------------------");
            
                for (MenuItem item : menuItems) {
                    System.out.println(item.displayDetails());
                }
                System.out.println("------------------------------------------------------------------------------------------------------------");
            }
        }
    }
}
