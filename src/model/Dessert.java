package model;

import java.util.Scanner;
import main.Main;

public class Dessert extends MenuItem implements Customizable{
    private String size = "Regular";

    public Dessert(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        size = "Regular";

        System.out.print("\n\033[1;33mCustomization\033[0m: Do you want a large size? Type 'yes' if yes, any character if just regular size: ");
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