package model;

import java.util.Scanner;
import main.Main;

public class Salad extends MenuItem implements Customizable{
    private String saltOption; 

    public Salad(int id, String name, String category, float price, int calories, int carbs, int protein, int fiber, int fat, int sodium, int sugar) {
        super(id, name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar);
        this.saltOption = "Normal Salt"; 
    }

    @Override
    public void customize() {
        Scanner scanner = Main.scanner;
        saltOption = "Normal";
        System.out.print("\n\033[1;33mCustomization\033[0m: Do you want less salt? Type 'yes' if yes, any character otherwise: ");
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