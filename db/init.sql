CREATE DATABASE  EZbyte;
USE EZbyte;

CREATE TABLE MENUITEM (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,                
    Name VARCHAR(100) NOT NULL,                           
    Category ENUM('Main Course', 'Sandwiches', 'Salads', 'Desserts', 'Beverages') NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,                       
    Calories INT NOT NULL,                               
    Carbs INT NOT NULL,                                   
    Protein INT NOT NULL,                                  
    Fiber INT NOT NULL,                              
    Fat INT NOT NULL,                                      
    Sodium INT NOT NULL,                             
    Sugar INT NOT NULL                                   
);


-- Pre-defined Menu for the system
-- Main Courses
INSERT INTO MenuItem (name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar)
VALUES
    ('Classic Meatball Spaghetti', 'Main Course', 175.00, 580, 78, 22, 5, 18, 780, 8),
    ('Tuna Alfredo Pasta', 'Main Course', 220.00, 510, 67, 30, 4, 15, 690, 6),
    ('Chicken Fettuccini Pesto', 'Main Course', 200.00, 595, 82, 35, 6, 22, 720, 3),
    ('Grilled Ribs with Garlic Rice', 'Main Course', 215.00, 622, 82, 45, 3, 32, 880, 5),
    ('Chicken Barbecue with Java Rice', 'Main Course', 190.00, 519, 74, 40, 3, 28, 840, 7);

-- Sandwiches
INSERT INTO MenuItem (name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar)
VALUES
    ('Classic BLT Sandwich', 'Sandwiches', 120.00, 380, 42, 15, 3, 14, 620, 5),
    ('Chicken Caesar Wrap', 'Sandwiches', 125.00, 420, 45, 25, 10, 18, 640, 4),
    ('Tuna Pesto Panini', 'Sandwiches', 145.00, 450, 48, 22, 12, 20, 700, 3),
    ('Avocado & Tuna Salad Wrap', 'Sandwiches', 130.00, 330, 37, 12, 3, 10, 580, 1),
    ('Grilled Tofu Burger', 'Sandwiches', 130.00, 390, 39, 18, 2, 17, 610, 2);

-- Salads
INSERT INTO MenuItem (name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar)
VALUES
    ('Classic Caesar Salad', 'Salads', 145.00, 214, 15, 7, 19, 12, 400, 1),
    ('Greek & Shredded Veggies', 'Salads', 160.00, 225, 12, 5, 19, 10, 330, 2),
    ('Chicken Garden Salad', 'Salads', 170.00, 296, 20, 25, 12, 8, 460, 3),
    ('Tuna Quinoa Salad', 'Salads', 165.00, 323, 35, 18, 10, 10, 420, 2),
    ('Fruit and Nut Salad', 'Salads', 120.00, 237, 40, 4, 11, 6, 250, 10);

-- Desserts
INSERT INTO MenuItem (name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar)
VALUES
    ('Mango Chia Pudding', 'Desserts', 100.00, 221, 30, 4, 6, 8, 20, 15),
    ('Coconut Yogurt with Fresh Fruits', 'Desserts', 90.00, 184, 25, 5, 4, 6, 25, 12),
    ('Chocolate Avocado Mousse', 'Desserts', 120.00, 256, 22, 4, 5, 10, 15, 8),
    ('Buko Salad with Fresh Coconut', 'Desserts', 110.00, 207, 28, 2, 3, 7, 10, 10),
    ('Banana Oat Energy Balls', 'Desserts', 60.00, 153, 20, 3, 3, 5, 5, 9);

-- Beverages
INSERT INTO MenuItem (name, category, price, calories, carbs, protein, fiber, fat, sodium, sugar)
VALUES
    ('Iced Coffee', 'Beverages', 70.00, 90, 15, 1, 0, 2, 60, 12),
    ('Fresh Mango Shake', 'Beverages', 85.00, 130, 35, 1, 1, 0, 15, 22),
    ('Lemon Iced Tea', 'Beverages', 65.00, 110, 27, 0, 0, 0, 40, 25),
    ('Banana Smoothie', 'Beverages', 100.00, 160, 35, 3, 5, 1, 30, 20),
    ('Buko Shake', 'Beverages', 75.00, 80, 20, 0, 2, 0, 20, 10);

CREATE TABLE USERACCOUNT (
    UserID INT PRIMARY KEY AUTO_INCREMENT, 
    Username VARCHAR(50) UNIQUE NOT NULL,    
    Password VARCHAR(255) NOT NULL,         
    PhoneNumber VARCHAR(15),              
    Address VARCHAR(255),                
    Balance FLOAT DEFAULT 0.00,             
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

CREATE TABLE USERCART (
    UserID INT,
    ItemID INT,
    Quantity INT DEFAULT 1,
    CustomizationDetails VARCHAR(255),
    Calories INT,
    Carbs INT,
    Protein INT,
    Fiber INT,
    Fat INT,
    Sodium INT,
    Sugar INT,
    PRIMARY KEY (UserID, ItemID, CustomizationDetails),
    FOREIGN KEY (UserID) REFERENCES USERACCOUNT(UserID),
    FOREIGN KEY (ItemID) REFERENCES MENUITEM(ItemID)
);


CREATE TABLE order_nutrition_history (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    OrderDate TIMESTAMP,
    TotalCalories INT,
    TotalCarbs INT,
    TotalProtein INT,
    TotalFat INT,
    TotalFiber INT,
    TotalSodium INT,
    TotalSugar INT,
    TotalPrice DECIMAL(10, 2),
    FOREIGN KEY (UserID) REFERENCES users(UserID)
);
