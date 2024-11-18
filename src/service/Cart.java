package service;

import connection.CartDatabaseHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import main.Main;
import model.*;

public class Cart {
    private final List<CartItem> usercart;
    private final UserAccount userAccount;
    private Connection connection;

    public Cart(Connection connection, UserAccount userAccount) {
        this.usercart = new ArrayList<>();
        this.userAccount = userAccount; 
        this.connection = connection;
    }
    
    public Connection getConnection() { return connection; }
    public List<CartItem> getCartItems() { return usercart; }
    public void setConnection(Connection connection) { this.connection = connection; }
    Scanner scanner = Main.scanner;

    public void addItem(MenuItem item, int quantity) {
        for (CartItem cartItem : usercart) { 
            if (cartItem.getMenuItem().getId() == item.getId() &&
                cartItem.getCustomization().equals(((Customizable)item).getCustomization())) { 
                cartItem.setQuantity(cartItem.getQuantity() + quantity); 
                saveCart();
                return; 
            }
        }
        usercart.add(new CartItem(item, quantity)); 
        saveCart();
    }
    
public void addItemToCart() {
    while (true) {
        System.out.println("\nSelect a category:");
        System.out.println("1. Main Course");
        System.out.println("2. Sandwiches");
        System.out.println("3. Salads");
        System.out.println("4. Desserts");
        System.out.println("5. Beverages");
        System.out.println("6. Go Back");
        System.out.print("Enter the number of your choice: ");

        int categoryChoice;
        try {
            categoryChoice = scanner.nextInt();  
            scanner.nextLine();  
        } catch (InputMismatchException e) {
            System.out.println("\n\t\t\t  =======================================================");
            System.out.println("\t\t\t                Please input a valid option.");
            System.out.println("\t\t\t  =======================================================");
            scanner.nextLine(); 
            continue;
        }

        if (categoryChoice == 6) {
            break; 
        }    

        String selectedCategory = getCategoryFromChoice(categoryChoice);
        if (selectedCategory == null) {
            System.out.println("\n\t\t\t  =======================================================");
            System.out.println("\t\t\t                Please input a valid option.");
            System.out.println("\t\t\t  =======================================================");
            continue;
            
        }
        String boldYellow = "\033[1m\033[33m";
        String reset = "\033[0m";
        System.out.printf("%s%55s%s%n", boldYellow, selectedCategory, reset);
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        
        List<MenuItem> itemsInCategory = fetchItemsFromCategory(selectedCategory);
        System.out.printf("%-50s | %-8s | %-4s | %-4s | %-4s | %-4s | %-4s | %-5s | %-4s%n",
                        "Item", "Price", "Cal", "Carb", "Prot", "Fib", "Fat", "Sod", "Sug");
        for (int i = 0; i < itemsInCategory.size(); i++) {
            MenuItem item = itemsInCategory.get(i);
            System.out.printf("%d. %s\n", i + 1, item.displayDetails());
        }
        System.out.println((itemsInCategory.size() + 1) + ". Go Back");
        System.out.print("\nSelect an item: ");

        int itemChoice;
        try {
            itemChoice = scanner.nextInt(); 
            scanner.nextLine();  
        } catch (InputMismatchException e) {
            System.out.println("\033[31m\n\t\t\t                Please input a valid option.\033[0m");
            scanner.nextLine();  
            continue;
        } if (itemChoice == itemsInCategory.size() + 1) {
            continue;
        }

        if (itemChoice < 1 || itemChoice > itemsInCategory.size()) {
            System.out.println("\033[31m\n\t\t\t                Please input a valid option.\033[0m");
            continue;
        }

        MenuItem selectedItem = itemsInCategory.get(itemChoice - 1);
    
        selectedItem.customize();

        int quantity;
        while (true) {
            System.out.print("Enter quantity: ");
            try {
                quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity > 0) {
                    break; 
                } else {
                    System.out.println("\n\t\t\t  =======================================================");
                    System.out.println("\033[31m\n\t\t\t   Quantity must be greater than zero. Please try again.\033[0m");
                    System.out.println("\t\t\t  =======================================================\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\033[31m\n\t\t\t  Invalid input. Please enter a valid number for quantity\033[0m");
                scanner.nextLine(); 
            }
        }
        addItem(selectedItem, quantity);
        System.out.println("\n\t\t\t\t-----------------------------------");
        System.out.println("\033[32m\t\t\t\t  Item added to cart successfully!\033[0m");
        System.out.println("\t\t\t\t-----------------------------------\n");
    }
}

private String getCategoryFromChoice(int choice) {
        return switch (choice) {
            case 1 -> "Main Course";
            case 2 -> "Sandwiches";
            case 3 -> "Salads";
            case 4 -> "Desserts";
            case 5 -> "Beverages";
            default -> null;
        };
}
private List<MenuItem> fetchItemsFromCategory(String category) {
    List<MenuItem> items = new ArrayList<>();
    try {
        String query = "SELECT ItemID, Category, Name, Price, Calories, Carbs, Protein, Fiber, Fat, Sodium, Sugar FROM MENUITEM WHERE Category = ?";
        PreparedStatement statement = getConnection().prepareStatement(query); 
        statement.setString(1, category);

        ResultSet resultSet = statement.executeQuery();


        while (resultSet.next()) {
            int id = resultSet.getInt("ItemID");
            String name = resultSet.getString("Name");
            float price = resultSet.getFloat("Price");
            int calories = resultSet.getInt("Calories");
            int carbs = resultSet.getInt("Carbs");
            int protein = resultSet.getInt("Protein");
            int fiber = resultSet.getInt("Fiber");
            int fat = resultSet.getInt("Fat");
            int sodium = resultSet.getInt("Sodium");
            int sugar = resultSet.getInt("Sugar");

            MenuItem item;

            switch (category) {
                case "Main Course" -> item = new MainCourse(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                case "Sandwiches" -> item = new Sandwich(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                case "Salads" -> item = new Salad(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                case "Desserts" -> item = new Dessert(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                case "Beverages" -> item = new Beverage(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
                default -> throw new IllegalArgumentException("Unknown category: " + category);
            }

            items.add(item);
        }

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error fetching items from category: " + category);
    }

    return items;
}

public void editItem() {
    if (usercart.isEmpty()) {
        System.out.println("\n\t\t    ===============================================================");
        System.out.println("\033[31m\t\t       Your cart is empty. Please add items to your cart first :)\033[0m");
        System.out.println("\t\t    ===============================================================\n");
        return;
    }

    int choice;
    while (true) {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Edit quantity of an item");
        System.out.println("2. Remove an item");
        System.out.println("3. Clear cart contents");
        System.out.println("4. Go back");

        System.out.print("Enter your choice: ");
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); 
            if (choice >= 1 && choice <= 4) {
                break; 
            } else {
                System.out.println("\033[31m\n\t\t\t             Invalid selection! Please try again.\033[0m");

            }
        } else {
            System.out.println("\033[31m\n\t\t\t             Invalid selection! Please try again.\033[0m");
            scanner.nextLine(); 
        }
    }

    switch (choice) {
        case 1 -> {
            int itemChoice;
            while (true) {
                System.out.println("\nYour current cart:");
                for (int i = 0; i < usercart.size(); i++) {
                    CartItem cartItem = usercart.get(i);
                    MenuItem item = cartItem.getMenuItem();
                    System.out.printf("%d. %s - x%d, Total Price: P %.2f Pesos\n",
                            i + 1, item.getName(), cartItem.getQuantity(),
                            cartItem.getTotalPrice());
                }    
                System.out.print("\nChoose an item to edit (enter the number): ");
                if (scanner.hasNextInt()) {
                    itemChoice = scanner.nextInt();
                    scanner.nextLine(); 
                    if (itemChoice >= 1 && itemChoice <= usercart.size()) {
                        break;
                    } else {
                        System.out.println("\033[31m\n\t\t\t             Invalid selection! Please try again.\033[0m");
                    }
                } else {
                    System.out.println("\033[31m\n\t\t\t                Please enter a valid number.\033[0m");
                    scanner.nextLine(); 
                }
            }

            CartItem selectedCartItem = usercart.get(itemChoice - 1);
            int newQuantity;
            while (true) {
                System.out.print("Enter the new quantity (must be greater than zero): ");
                if (scanner.hasNextInt()) {
                    newQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (newQuantity > 0) {
                        break; // Valid quantity, exit loop
                    } else {
                        System.out.println("\n\t\t\t  =======================================================");
                        System.out.println("\033[31m\t\t\t  Invalid quantity entered. Please enter a positive value\033[0m");
                        System.out.println("\t\t\t  =======================================================\n");
                    }
                } else {
                    System.out.println("\033[31m\t\t\t                Please enter a valid number.\033[0m");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            selectedCartItem.setQuantity(newQuantity);
            System.out.println("\n\t\t\t\t  -----------------------------------");
            System.out.println("\033[32m\t\t\t\t  Item quantity updated successfully!\033[0m");
            System.out.println("\t\t\t\t  -----------------------------------");
            saveCart();
            }
        case 2 -> {
                int itemChoice;
                while (true) {
                    System.out.println("\nYour current cart:");
                    for (int i = 0; i < usercart.size(); i++) {
                        CartItem cartItem = usercart.get(i);
                        MenuItem item = cartItem.getMenuItem();
                        System.out.printf("%d. %s - x%d, Total Price: P %.2f Pesos\n",
                                i + 1, item.getName(), cartItem.getQuantity(),
                                cartItem.getTotalPrice());
                    }
                    System.out.print("\nChoose an item to remove (enter the number): ");
                    if (scanner.hasNextInt()) {
                        itemChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (itemChoice >= 1 && itemChoice <= usercart.size()) {
                            break; // Valid selection, exit loop
                        } else {
                            System.out.println("\033[31m\n\t\t\t             Invalid selection! Please try again.\033[0m");

                        }
                    } else {
                        System.out.println("\033[31m\n\t\t\t                Please enter a valid number.\033[0m");
                        scanner.nextLine(); // Clear invalid input
                    }
                }   CartItem selectedCartItem = usercart.get(itemChoice - 1);
                selectedCartItem.markForDeletion();
                saveCart();
                selectedCartItem.resetDeletionFlag();
                usercart.remove(selectedCartItem);
                System.out.println("\n\t\t\t\t ------------------------------------");
                System.out.println("\033[32m\t\t\t\t Item removed from cart successfully!\033[0m");
                System.out.println("\t\t\t\t ------------------------------------");
            }

        case 3 -> {
            clearCart();
            System.out.println("\n\t\t\t     ----------------------------------------------");
            System.out.println("\033[32m\t\t\t        Your cart has been cleared successfully!\033[0m");
            System.out.println("\t\t\t     ----------------------------------------------\n");
            }

        case 4 -> {}
        default -> {
            System.out.println("\033[31m\n\t\t\t             Invalid selection! Please try again.\033[0m");
            }
    }
}

public void displayCart() {
    System.out.println("\033[1;33m\n\t\t\t\t\t             Your Cart:\033[0m");

    System.out.println("\033[1;33m---------------------------------------------------------------------------------------------------------------------------------\033[0m");
    System.out.printf("%-50s | %-8s | %-4s | %-4s | %-4s | %-4s | %-4s | %-5s | %-3s | %-20s%n",
                    "Item", "Price", "Cal", "Carb", "Prot", "Fib", "Fat", "Sod", "Sug", "Notes");
    System.out.println("\033[1;33m---------------------------------------------------------------------------------------------------------------------------------\033[0m");

    for (CartItem cartItem : usercart) {
        MenuItem item = cartItem.getMenuItem();
        int quantity = cartItem.getQuantity();
        System.out.println("x" + quantity + " " + item.displayDetails() + " | " + cartItem.getCustomization());
    }

    System.out.println("\033[1;33m---------------------------------------------------------------------------------------------------------------------------------\033[0m");
    System.out.printf("%-50s | P %-6.2f | %-4d | %-3dg | %-3dg | %-3dg | %-3dg | %-3dmg | %-2dg|\n", "Total:",
            calculateTotalCost(),
            calculateTotalCalories(),
            calculateTotalCarbs(),
            calculateTotalProtein(),
            calculateTotalFiber(),
            calculateTotalFat(),
            calculateTotalSodium(),
            calculateTotalSugar());
    System.out.println("\033[1;33m---------------------------------------------------------------------------------------------------------------------------------\033[0m");
}
    public void clearCart() {
        usercart.clear();
        try {
            String sql = "DELETE FROM usercart WHERE UserID = ?";
            try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
                pstmt.setInt(1, userAccount.getUserId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return usercart.isEmpty();
    }
    public void saveCart() {
        CartDatabaseHandler.saveCartToDatabase(usercart, userAccount.getUserId(), getConnection());
    }

    public void loadCart() {
        CartDatabaseHandler.loadCartFromDatabase(usercart, userAccount.getUserId(), getConnection());
    }

    public float calculateTotalCost() {
        float totalCost = 0;
        for (CartItem cartItem : usercart) {
            totalCost += cartItem.getTotalPrice();
        }
        return totalCost;
    }

    public int calculateTotalCalories() {
        int totalCalories = 0;
        for (CartItem cartItem : usercart) {
            totalCalories += cartItem.getMenuItem().getCalories() * cartItem.getQuantity();
        }
        return totalCalories;
    }

    public int calculateTotalCarbs() {
        int totalCarbs = 0;
        for (CartItem cartItem : usercart) {
            totalCarbs += cartItem.getMenuItem().getCarbs() * cartItem.getQuantity();
        }
        return totalCarbs;
    }

    public int calculateTotalProtein() {
        int totalProtein = 0;
        for (CartItem cartItem : usercart) {
            totalProtein += cartItem.getMenuItem().getProtein() * cartItem.getQuantity();
        }
        return totalProtein;
    }

    public int calculateTotalFiber() {
        int totalFiber = 0;
        for (CartItem cartItem : usercart) {
            totalFiber += cartItem.getMenuItem().getFiber() * cartItem.getQuantity();
        }
        return totalFiber;
    }
    public int calculateTotalFat() {
        int totalFat = 0;
        for (CartItem cartItem : usercart) {
            totalFat += cartItem.getMenuItem().getFat() * cartItem.getQuantity();
        }
        return totalFat;
    }

    public int calculateTotalSodium() {
        int totalSodium = 0;
        for (CartItem item : usercart) {
            totalSodium += item.getMenuItem().getSodium() * item.getQuantity();
        }
        return totalSodium;
    }
    
    public int calculateTotalSugar() {
        int totalSugar = 0;
        for (CartItem item : usercart) {
            totalSugar += item.getMenuItem().getSugar() * item.getQuantity();
        }
        return totalSugar;
    }    

}
