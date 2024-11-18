package model;

import java.util.Scanner;
import main.Main;

public class Sandwich extends MenuItem implements Customizable {
    private String customization; 

    public Sandwich(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);

        this.customization = "No Customizations"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;

        StringBuilder removedItemsBuilder = new StringBuilder();

        System.out.print("\n\033[1;33mCustomization\033[0m: Do you want to remove lettuce? Type 'yes' to remove, any other key to keep: ");

        String inputLettuce = scanner.nextLine().trim().toLowerCase();
        if (inputLettuce.equals("yes")) {
            this.calories -= 5;  
            this.fiber -= 1;     
            removedItemsBuilder.append("No Lettuce, ");
        }

        System.out.print("\033[1;33mCustomization\033[0m: Do you want to remove onion? Type 'yes' to remove, any other key to keep: ");
        String inputOnion = scanner.nextLine().trim().toLowerCase();
        if (inputOnion.equals("yes")) {
            this.calories -= 10;  
            this.fiber -= 1;    
            removedItemsBuilder.append("No Onion, ");
        }

        System.out.print("\033[1;33mCustomization\033[0m: Do you want to remove tomato? Type 'yes' to remove, any other key to keep: ");
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