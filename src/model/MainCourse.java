package model;

import java.util.Scanner;
import main.Main;

public class MainCourse extends MenuItem implements Customizable {
    private String servingSize = "Normal";

    public MainCourse(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        servingSize = "Normal Serving";
        System.out.print("\n\033[1;33mCustomization\033[0m: Do you want a half serving? Type 'yes' if yes, any character otherwise: ");
        String input = scanner.nextLine().trim().toLowerCase(); 

        if (input.equals("yes")) {
            servingSize = "Half Serving";
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
