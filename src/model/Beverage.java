package model;

import java.util.Scanner;
import main.Main;

public class Beverage extends MenuItem implements Customizable{
    private String size;

    public Beverage(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
        this.size = "Medium"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;

        System.out.print("\nChoose a size: type '\033[1;33mSmall\033[0m', '\033[1;33mMedium\033[0m', or '\033[1;33mLarge\033[0m': ");
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