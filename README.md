# EzByte - Healthy Food Ordering System

## I. Project Overview

EzByte Health is a Java-based healthy food ordering system designed to provide users with a nutrition-focused ordering experience. The system allows users to explore a variety of healthy menu options, customize their choices, add items to a personal cart, and manage their transactions through an in-app wallet or a cash-on-delivery method. The project emphasizes user convenience and encourages healthier dietary choices by calculating the nutritional information of each item in the cart. Data is managed with a MySQL database to store user information, cart items, and transactional details, ensuring a persistent and reliable experience.

## II. Application of Object-Oriented Programming (OOP) Principles

EzByte Health was developed with key OOP principles in mind to ensure a modular, maintainable, and scalable codebase. Here’s how each principle was applied:

- **Encapsulation**: Key attributes and methods of classes such as `MenuItem`, `UserAccount`, and `CartItem` are encapsulated to protect data integrity and maintain clear access control. Getters and setters are used to allow controlled access to data, and the MySQL integration is abstracted within database-related classes.

- **Inheritance**: The `MenuItem` class serves as a base class, with specialized subclasses (like `MainCourse` and other categories) to handle unique item types and customizations. This hierarchical structure enables code reuse and simplifies the extension of the menu by adding more item types if needed.

- **Polymorphism**: Polymorphism is used in handling different types of menu items and their display formats. For instance, each subclass of `MenuItem` has its own `customize()` method to provide unique customization options. Methods like `displayDetails()` are overridden in subclasses to format details based on specific requirements.

- **Abstraction**: Classes like `UserAccount`, `Cart`, and `Order` encapsulate complex logic to simplify the main program flow. By abstracting functionalities such as cart management, user authentication, and payment handling, EzByte Health maintains a clear separation of concerns, making it easier to manage and extend the system.

## III. Integration of Sustainable Development Goal 3 (Good Health and Well-being)

EzByte Health aligns with Sustainable Development Goal 3 (SDG 3), which aims to promote health and well-being. The project encourages healthier dietary choices by providing users with detailed nutritional information for each item and allowing for customizations like portion control and adjusting of ingredients. By integrating nutritional tracking, EzByte empowers users to make informed decisions about their meals, supporting better nutrition and overall health.

## IV. Instructions for Running the Program

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure that you have JDK 8 or higher installed on your system.
2. **MySQL Server**: Set up MySQL server and create a database to store project data.

### Setup Instructions

1. **Clone or Download the Repository**:
   - Clone the repository:
     ```bash
     git clone https://github.com/your-username/ezbyte-health.git
     cd ezbyte-health
     ```
   - Alternatively, you can download the repository as a ZIP file and extract it.

2. **Configure the Database**:
   - Create a new MySQL database.
   - Import the provided SQL schema file (`schema.sql`) to set up the required tables.

3. **Update Database Configuration**:
   - Open the `DBConnection` class in your codebase.
   - Replace the placeholder values for the database URL, username, and password with your MySQL credentials:
     ```java
     // Example:
     String url = "jdbc:mysql://localhost:3306/your_database_name";
     String username = "your_username";
     String password = "your_password";
     ```

4. **Compile and Run the Program**:
   - Navigate to the project directory and compile the program:
     ```bash
     javac Main.java
     ```
   - Run the program:
     ```bash
     java Main
     ```

### Usage

- **Register/Login**: Register a new account or log in to access the menu and other features.
- **Add Items to Cart**: Explore the menu, customize items as needed, and add them to your cart.
- **View Cart**: Check the items in your cart along with their nutritional details.
- **Checkout**: Proceed to checkout and pay through your EzByte wallet or opt for cash on delivery.
