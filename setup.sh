#!/bin/bash

echo "🔧 Инициализация проекта Booking..."

# 1. Убить процесс на порту 5000
echo "🛑 Завершаю процесс на порту 5000..."
lsof -ti:5000 | xargs kill -9 2>/dev/null

# 2. Пересоздать базу данных
echo "🗄️  Пересоздаю базу данных..."
mysql -u root -p@LinDu098 << 'EOF'
DROP DATABASE IF EXISTS booking_system;
CREATE DATABASE booking_system;
USE booking_system;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('GUEST', 'OWNER', 'ADMIN') DEFAULT 'GUEST',
    full_name VARCHAR(100) DEFAULT '',
    email VARCHAR(120) DEFAULT '',
    phone VARCHAR(40) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100),
    address VARCHAR(200),
    stars INT DEFAULT 0,
    price INT DEFAULT 0,
    rating DOUBLE DEFAULT 0.0,
    description TEXT,
    image_url VARCHAR(500),
    owner_id INT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    property_type VARCHAR(30) DEFAULT 'Hotel',
    district VARCHAR(80),
    distance_to_center DOUBLE DEFAULT 1.0,
    popularity INT DEFAULT 0,
    available_rooms INT DEFAULT 5,
    has_wifi BOOLEAN DEFAULT TRUE,
    has_parking BOOLEAN DEFAULT FALSE,
    has_pool BOOLEAN DEFAULT FALSE,
    breakfast_included BOOLEAN DEFAULT FALSE,
    free_cancellation BOOLEAN DEFAULT TRUE,
    gallery_urls TEXT,
    policies TEXT,
    room_options TEXT,
    contact_email VARCHAR(120),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hotel VARCHAR(200) NOT NULL,
    booking_date VARCHAR(20),
    check_out VARCHAR(20),
    guests INT DEFAULT 1,
    rooms INT DEFAULT 1,
    guest_name VARCHAR(100) DEFAULT '',
    guest_email VARCHAR(120) DEFAULT '',
    guest_phone VARCHAR(40) DEFAULT '',
    status VARCHAR(30) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    user_id INT NOT NULL,
    booking_id INT DEFAULT NULL,
    rating INT DEFAULT 5,
    comment TEXT,
    cleanliness INT DEFAULT 5,
    comfort INT DEFAULT 5,
    staff INT DEFAULT 5,
    photo_url VARCHAR(500) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE wishlists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hotel_id INT NOT NULL,
    collection_name VARCHAR(80) DEFAULT 'Favorites',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_wishlist_item (user_id, hotel_id, collection_name),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE recent_searches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    query_text VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
EOF

# 3. Скомпилировать проект
echo "🔨 Компилирую Java файлы..."
find src -name "*.java" -exec javac --module-path ~/Downloads/javafx-sdk-26.0.1/lib --add-modules javafx.controls,javafx.fxml -cp lib/mysql-connector-j-8.0.33.jar -d bin {} +

# 4. Запустить сервер
echo "✅ Сервер запускается на порту 5000..."
java -cp bin:lib/mysql-connector-j-8.0.33.jar network.BookingServer
