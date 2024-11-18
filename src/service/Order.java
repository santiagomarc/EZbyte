package service;

import connection.OrderDBHandler;
import java.sql.Connection;
import java.util.Scanner;
import main.Main;

public class Order {
    private UserAccount user;
    private Cart cart;
    private String paymentMode;
    private float totalCost;
    private float cashAmount;
    private float change;
    private Connection connection;

    public Order(UserAccount userAccount, Cart cart) {
        this.user = userAccount;
        this.cart = cart;
    }

    public UserAccount getUser() { return user; }
    public Cart getCart() { return cart; }
    public String getPaymentMode() { return paymentMode; }
    public float getTotalCost() { return totalCost; }
    public float getCashAmount() { return cashAmount; }
    public float getChange() { return change; }
    public Connection getConnection() { return connection; }

    public void setUser(UserAccount user) { this.user = user; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public void setTotalCost(float totalCost) { this.totalCost = totalCost; }
    public void setCashAmount(float cashAmount) { this.cashAmount = cashAmount; }
    public void setChange(float change) { this.change = change; }
    public void setConnection(Connection connection) { this.connection = connection; }

    Scanner scanner = Main.scanner;

    public void processOrder() {
        if (cart.isEmpty()) {
            System.out.println("\n\t\t    ===============================================================");
            System.out.println("\033[31m\t\t    Your cart is empty. Please add items to proceed with your order\033[0m");
            System.out.println("\t\t    ===============================================================");
            return;
        }

        cart.displayCart();
        setTotalCost(cart.calculateTotalCost());

        System.out.print("\nProceed with this order? Type '\033[1;34myes\033[0m' to proceed, any character to cancel: ");
        if (!scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("Order canceled. Returning to previous menu.");
            return;
        }

        System.out.println("\n\033[1;34mSelect payment method:\033[0m");
        System.out.printf("1. Ezbyte Wallet (Current balance: %.2f pesos)\n", user.getBalance());
        System.out.println("2. Cash-on-Delivery");

        int paymentChoice = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("\nEnter your choice: ");
            if (scanner.hasNextInt()) {
                paymentChoice = scanner.nextInt();
                scanner.nextLine();
                if (paymentChoice == 1 || paymentChoice == 2) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice. Please enter 1 for Ezbyte Wallet or 2 for Cash-on-Delivery.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric choice.");
                scanner.nextLine();
            }
        }

        switch (paymentChoice) {
            case 1 -> {
                setPaymentMode("Ezbyte Wallet");
                if (user.getBalance() < getTotalCost()) {
                    System.out.println("\033[1;31m\n\t\t      Insufficient balance. Please top up your wallet to proceed.\033[0m");
                    return;
                }
                setCashAmount(getTotalCost());
                user.setBalance(user.getBalance() - getTotalCost());
                setChange(0);
                OrderDBHandler.updateBalanceInDB(getConnection(), user.getUserId(), user.getBalance());
                System.out.println("\nSuccessful! Thank you for using EZbyte wallet!");
            }
            case 2 -> {
                setPaymentMode("Cash-on-Delivery");
                System.out.print("Enter cash amount to be paid: ");
                
                if (scanner.hasNextFloat()) {
                    float cashAmount = scanner.nextFloat();
                    if (cashAmount >= getTotalCost() && cashAmount > 0) {
                        setCashAmount(cashAmount);
                        setChange(getCashAmount() - getTotalCost());
                        System.out.printf("\nPayment successful via Cash-on-Delivery. Your change: %.2f pesos\n", getChange());
                    } else {
                        System.out.println("\033[1;31m\n\tInsufficient or invalid amount. Please enter an amount greater than or equal to the total cost.\033[0m");
                        return; 
                    }
                } else {
                    System.out.println("\033[1;31m\n\t\t\t Invalid input. Please enter a valid amount in numbers.\033[0m");
                    scanner.nextLine(); 
                    return; 
                }
            }
            default -> {
                System.out.println("\nInvalid payment method selected. Returning to previous menu.");
                return;
            }
        }
        OrderDBHandler.saveOrderHistoryToDB(getConnection(), getUser(), getCart());
        displayOrderSummary();
    }

    public void displayOrderSummary() {
    System.out.println("\n\t      ===================================== \033[1;33mOrder Summary\033[0m =======================================");
    System.out.println("\t      Customer Name: " + user.getUsername());
    System.out.println("\t      Delivery Address: " + user.getAddress());
    System.out.println("\t      Phone Number: " + user.getPhoneNumber());
    System.out.println("\t      Payment Mode: " + getPaymentMode());
    System.out.printf("\t      Payment Amount: %.2f Pesos\n", getCashAmount());
    System.out.printf("\t      Change: %.2f Pesos\n\n", getChange());
    System.out.println("\t      ===========================================================================================");
    System.out.println("\t                                            \033[1;33mOrdered Items\033[0m                                      ");
    System.out.println("\t      ===========================================================================================");
    System.out.println();
    cart.displayCart();
    System.out.println("\n\t\t           -----------------------------------------------------------------");
    System.out.println("\033[32m\t\t           Thank you for using EZbyte! Your meal will be processed shortly!\033[0m");
    System.out.println("\t\t           ----------------------------------------------------------------");
    cart.clearCart();
    }
}
