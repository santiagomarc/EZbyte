import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

interface Customizable {
    String getCustomization();
}

class MainCourse extends MenuItem implements Customizable {
    private String servingSize = "Normal";

    public MainCourse(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        servingSize = "Normal";
        System.out.print("\nDo you want a half serving? Type 'yes' if yes, any character otherwise: ");
        String input = scanner.nextLine().trim().toLowerCase(); 

        if (input.equals("yes")) {
            servingSize = "Half";
            this.calories /= 2;
            this.carbs /= 2;
            this.protein /= 2;
            this.fiber /= 2;
            this.fat /= 2;
            this.sodium /= 2;
            this.sugar /= 2;
        }
    }

    @Override
    public String displayDetails() {
        return String.format("%-33s (Main Course) | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg",
                name, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public String getCustomization() {
        return servingSize; 
    }
}


class Sandwich extends MenuItem implements Customizable {
    private String customization; 

    public Sandwich(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);

        this.customization = "No Customizations"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;

        StringBuilder removedItemsBuilder = new StringBuilder();

        System.out.print("\nDo you want to remove lettuce? Type 'yes' to remove, any other key to keep: ");
        String inputLettuce = scanner.nextLine().trim().toLowerCase();
        if (inputLettuce.equals("yes")) {
            this.calories -= 5;  
            this.fiber -= 1;     
            removedItemsBuilder.append("No Lettuce, ");
        }

        System.out.print("\nDo you want to remove onion? Type 'yes' to remove, any other key to keep: ");
        String inputOnion = scanner.nextLine().trim().toLowerCase();
        if (inputOnion.equals("yes")) {
            this.calories -= 10;  
            this.fiber -= 1;    
            removedItemsBuilder.append("No Onion, ");
        }

        System.out.print("\nDo you want to remove tomato? Type 'yes' to remove, any other key to keep: ");
        String inputTomato = scanner.nextLine().trim().toLowerCase();
        if (inputTomato.equals("yes")) {
            this.calories -= 10; 
            this.sugar -= 1;      
            removedItemsBuilder.append("No Tomato, ");
        }

        if (removedItemsBuilder.length() > 0) {
            customization = removedItemsBuilder.substring(0, removedItemsBuilder.length() - 2);
        } else {
            customization = "No Customizations";
        }
    }

    @Override
    public String displayDetails() {
        return String.format("%-33s (Sandwich)    | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg",
                name, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public String getCustomization() {
        return customization;
    }
}

class Salad extends MenuItem implements Customizable{
    private String saltOption; 

    public Salad(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
        this.saltOption = "Normal"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        saltOption = "Normal";
        System.out.print("\nDo you want less salt? Type 'yes' if yes, any character otherwise: ");
        String input = scanner.nextLine().trim().toLowerCase();  

        if (input.equals("yes")) {
            saltOption = "Less Salt";
            this.sodium /= 2; 
        }
    }

    @Override
    public String displayDetails() {
        return String.format("%-33s (Salad)       | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg",
                name, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public String getCustomization() {
        return saltOption; 
    }
}

class Dessert extends MenuItem implements Customizable{
    private String size = "Regular";

    public Dessert(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        size = "Regular";

        System.out.print("\nDo you want a large size? Type 'yes' if yes, any character if just regular size: ");
        String input = scanner.nextLine().trim().toLowerCase(); 

        if (input.equals("yes")) {
            size = "Large";
            this.calories *= 1.25;
            this.sugar *= 1.25;
            this.fat *= 1.25;
            this.carbs *= 1.25;
        }
    }

    @Override
    public String displayDetails() {
        return String.format("%-33s (Dessert)     | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg",
                name, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public String getCustomization() {
        return size; 
    }
}



class Beverage extends MenuItem implements Customizable{
    private String size;

    public Beverage(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
        this.size = "Medium"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;

        System.out.print("\nChoose a size type Small, Medium, or Large: ");
        String input = scanner.nextLine().trim().toLowerCase(); 

        switch (input) {
            case "small" -> {
                size = "Small";
                this.calories *= 0.75;  
                this.sugar *= 0.75;  
            }
            case "large" -> {
                size = "Large";
                this.calories *= 1.25;  
                this.sugar *= 1.25; 
            }
            case "medium" -> size = "Medium"; 
            default -> {
                size = "Medium"; 
                System.out.println("Invalid input. Added as Medium (default) .");
            }
        }
    }

    @Override
    public String displayDetails() {
        return String.format("%-33s (Beverage)    | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg",
                name, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public String getCustomization() {
        return size; 
    }
}
