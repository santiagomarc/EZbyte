# 🍎 EZByte - Healthy Food Ordering System

## I. Project Overview 🌟

EZByte is a Java-based healthy food ordering system designed to provide users with a nutrition-focused ordering experience. The system allows users to explore a variety of healthy menu options, customize their choices, add items to a personal cart, and manage their transactions through an in-app wallet or a cash-on-delivery method. The project emphasizes user convenience and encourages healthier dietary choices by calculating the nutritional information of each item in the cart. Data is managed with a MySQL database to store user information, cart items, and transactional details, ensuring a persistent and reliable experience!

### Key Features ✨

- 🔑 **Register/Login**: Register a new account or log in to access the menu and other features.
- 📋 **Display Menu**: Display the pre-defined menu of the program to make your choices in advance.
- 🛒 **Add Items to Cart**: Explore the menu, customize items as needed, and add them to your cart.
- ✏️ **Edit Items**: Edit the quantity of an item, remove an item, or clear the content of your cart.
- 🧾 **View Cart**: Check the items in your cart along with their nutritional details.
- ✅ **Checkout**: Proceed to checkout and pay through your EzByte wallet or opt for cash on delivery.
- 🍔 **View Order History**: Check your order history to track the total nutrtional details of your past orders.
- 👤 **User Features**: Top-up your EZbyte wallet and check your credentials.

### MySQL Integration 💾
This project integrates a MySQL database to store and retrieve menu items, user data, and order details. The integration ensures data persistence, enabling functionality like saving user carts to the database, loading back the cart content of the user from the database upon logging in, and processing payments.
### Error and Input Handling 🚩
Proper Error and Input Handling is implemented all through out the program to ensure that it runs smoothly even when unexpected issues occur such as invalid inputs or trying to edit the cart when it is empty.


## II. Application of Object-Oriented Programming (OOP) Principles 💡

- 🔒 **Encapsulation**: Key attributes and methods of classes such as `MenuItem`, `UserAccount`, and `CartItem` are encapsulated to protect data integrity and maintain clear access control. Getters and setters are used to allow controlled access to data, and the MySQL integration is abstracted within database-related classes. 

- 📂 **Inheritance**: The `MenuItem` class serves as a base class, with subclasses (like `MainCourse` and other categories) to handle unique item types and customizations. This hierarchical structure enables code reuse and simplifies the extension of the menu by adding more item types if needed. This reduces code duplication and promotes code reuse, as all menu items share attributes like name, price, and nutritional facts.

- 🔄 **Polymorphism**: Polymorphism is used in handling different types of menu items and their display formats. For instance, each subclass of `MenuItem` has its own `customize()` method to provide unique customization options. Methods like `displayDetails()` are overridden in subclasses to format details based on specific requirements. These methods are defined abstractly in the MenuItem class and implemented differently by each subclass. When a MenuItem object is referenced, the program dynamically determines which version of the method to execute based on the actual type of the object. For example, calling customize on a MainCourse object will invoke its specific implementation, while calling it on a Dessert object will execute a different one, allowing the system to handle diverse menu items uniformly.

- 🚀 **Abstraction**: The MenuItem class is abstract, which enforces a common structure for its subclasses while delegating specific implementations of methods like `displayDetails()` and `customize()` to the subclasses. The `Customizable` interface defines a contract for all menu items that can be customized. Each subclass implements `getCustomization()` differently, adhering to the contract while providing unique behavior based on the object type.

## III. Integration of Sustainable Development Goal 3 (Good Health and Well-being) ❤️‍🩹

EZbyte aligns with Sustainable Development Goal 3 (SDG 3), which aims to promote health and well-being. The project encourages healthier dietary choices by providing users with detailed nutritional information for each item and allowing for customizations like portion control and adjusting of ingredients. By integrating nutritional tracking, EzByte encourages users to make informed decisions about their meals, supporting better nutrition and overall health.

## IV. Instructions for Running the Program 🖥️

### Setup Instructions 🛠️

1. 📥 **Clone or Download the Repository**:
   - Clone the repository:
     ```bash
     git clone https://github.com/your-username/EZbyte.git
     cd ezbyte
     ```
   - Alternatively, you can download the repository as a ZIP file and extract it.

2. 🗄️ **Configure the Database**:
   - Create a new MySQL database.
   - Import the provided SQL schema file (`init.sql`) to set up the database and run it in your workbench to create the necessary tables.

3. ⚙️ **Update Database Configuration**:
   - Open the `DBConnection` class in your codebase.
   - Replace the placeholder values for the database URL, username, and password with your MySQL credentials:
     ```java
     // Example:
     String url = "jdbc:mysql://localhost:3306/your_database_name";
     String username = "your_username";
     String password = "your_password";
     ```

4. 🏃‍♂️‍➡️ **Compile and Run the Program**:
   - Navigate to the 'src' directory and compile the main program:
     ```bash
     javac main/Main.java
     ```
   - Run the program:
     ```bash
     java main/Main
     ```

