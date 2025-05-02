CREATE DATABASE calorie_tracker;


CREATE TABLE food_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            food_name VARCHAR(100) NOT NULL,
                            calories_per_serving INT NOT NULL,
                            image_path VARCHAR(255),  -- Added for image path
                            meal_type VARCHAR(50) DEFAULT 'Unknown'  -- Added for meal type
);


CREATE TABLE user_log (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          food_id INT NOT NULL,
                          servings INT NOT NULL,
                          total_calories INT NOT NULL,
                          meal_type VARCHAR(20) NOT NULL DEFAULT 'Unknown',
                          log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (food_id) REFERENCES food_items(id) ON DELETE CASCADE
);

CREATE TABLE weight_log (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            log_date DATE NOT NULL,
                            weight DOUBLE NOT NULL
);

INSERT INTO food_items (food_name, calories_per_serving, image_path, meal_type) VALUES
('Boiled Egg', 78, '/path/to/image/boiled_egg.jpg', 'Breakfast'),
('Apple', 95, '/path/to/image/apple.jpg', 'Snack'),
('Grilled Chicken Breast (100g)', 165, '/path/to/image/grilled_chicken.jpg', 'Lunch'),
('Rice (1 cup)', 200, '/path/to/image/rice.jpg', 'Lunch'),
('Chapati (1 piece)', 120, '/path/to/image/chapati.jpg', 'Dinner'),
('Milk (1 glass)', 150, '/path/to/image/milk.jpg', 'Breakfast'),
('Banana', 105, '/path/to/image/banana.jpg', 'Snack'),
('Paneer (100g)', 265, '/path/to/image/paneer.jpg', 'Dinner'),
('Banana', 105, '/path/to/image/banana.jpg', 'Snack'),
('Paneer (100g)', 265, '/path/to/image/paneer.jpg', 'Dinner');

DESCRIBE weight_log;