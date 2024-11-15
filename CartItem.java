public class CartItem {
    private final MenuItem menuItem; 
    private int quantity;
    private boolean markedForDeletion = false;
    private String customization; 

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        if (menuItem instanceof Customizable customizable) {
            this.customization = customizable.getCustomization(); 
        } 
    }
    public CartItem(MenuItem menuItem, int quantity, String customization) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.customization = customization; 
    }


    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getTotalPrice() { return menuItem.getPrice() * getQuantity(); }
    public String getCustomization() { return customization; }

    public boolean isMarkedForDeletion() { return markedForDeletion; }
    public void markForDeletion() {this.markedForDeletion = true;}
    public void resetDeletionFlag() {this.markedForDeletion = false;}

}
