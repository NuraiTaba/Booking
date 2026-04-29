# Booking System

Java hotel booking application with JavaFX GUI, socket server, and MySQL database.

## Features

- **User Management**: Registration, login, user roles (Guest, Owner, Admin)
- **Hotel Search**: Search hotels by city, price range, ratings
- **Booking System**: Create and manage hotel bookings
- **Wishlist**: Save favorite hotels to collections
- **Owner Dashboard**: Manage own hotels and bookings
- **Admin Panel**: Manage all users and hotels
- **Socket-based Architecture**: Client-server communication on port 6000

## Prerequisites

- **Java**: OpenJDK 17+
- **MySQL**: MySQL 8.0+
- **JavaFX SDK**: v26.0.1 (download from https://gluonhq.com/products/javafx/)
- **MySQL JDBC Driver**: `lib/mysql-connector-j-8.0.33.jar` (included)

## Installation

### 1. Setup Database

```bash
mysql -u root -p
CREATE DATABASE booking_system;
EXIT;
```

### 2. Compile Project

```bash
cd /path/to/Booking
javac --module-path ~/Downloads/javafx-sdk-26.0.1/lib \
      --add-modules javafx.controls,javafx.fxml \
      -cp src:lib/mysql-connector-j-8.0.33.jar \
      src/**/*.java
```

### 3. Start the Server

Open Terminal 1:
```bash
cd /path/to/Booking
java -cp src:lib/mysql-connector-j-8.0.33.jar network.BookingServer
```

Output should show: `✅ Server started on port 6000`

### 4. Run the GUI Application

Open Terminal 2:
```bash
cd /path/to/Booking
java --module-path ~/Downloads/javafx-sdk-26.0.1/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp src:lib/mysql-connector-j-8.0.33.jar \
     gui.LoginApp
```

## Demo Accounts

After server startup, the following test accounts are created:

| Username | Password | Role  |
|----------|----------|-------|
| demo_owner | demo | Owner |
| demo_guest | demo | Guest |
| admin    | admin | Admin |

## Usage

### Guest User
- Search and view hotels
- Create bookings
- Manage wishlist
- View profile

### Hotel Owner
- Add and manage own hotels
- View bookings for own hotels
- Edit hotel details

### Admin User
- View all users
- Manage all hotels
- Monitor system activity

## Project Structure

```
src/
  ├── Main.java              # Entry point
  ├── gui/
  │   └── LoginApp.java      # JavaFX GUI
  ├── controller/
  │   └── BookingController.java
  ├── service/
  │   ├── AuthService.java
  │   └── BookingService.java
  ├── repository/
  │   └── DatabaseRepository.java
  ├── network/
  │   ├── BookingServer.java
  │   └── SocketClient.java
  ├── model/
  │   ├── User.java
  │   ├── Hotel.java
  │   ├── Booking.java
  │   └── Review.java
  ├── exception/
  │   ├── AppException.java
  │   ├── DatabaseException.java
  │   └── ValidationException.java
  ├── handler/
  │   └── RequestHandler.java
  ├── interfaces/
  │   ├── IAuthService.java
  │   └── IBookingService.java
  └── util/
      └── FileLogger.java

lib/
  └── mysql-connector-j-8.0.33.jar

```

## Architecture

### Client-Server Communication
- **Port**: 6000
- **Protocol**: Plain text with `|` or space delimiters
- **Examples**:
  - `LOGIN username password` → `SUCCESS userId role`
  - `REGISTER username password` → `SUCCESS User registered`
  - `BOOK userId hotelName date` → `SUCCESS Booking created`

### Database Schema
- **users**: User accounts and roles (GUEST, OWNER, ADMIN)
- **hotels**: Hotel listings with owner references
- **bookings**: Guest bookings with dates and status
- **reviews**: Guest reviews and ratings
- **wishlists**: User favorite hotels collections

## Testing

Run socket client tests:
```bash
java -cp src:lib/mysql-connector-j-8.0.33.jar network.SocketClient
```

## Troubleshooting

### MySQL Connection Error
- Ensure MySQL is running: `mysql -u root -p`
- Check connection string in `DatabaseRepository.java`
- Default: `jdbc:mysql://localhost:3306/booking_system`

### Port 6000 Already in Use
```bash
lsof -iTCP:6000 -sTCP:LISTEN -P -n | tail -1 | awk '{print $2}' | xargs kill -9
```

### JavaFX Module Error
- Verify JavaFX SDK path in compile/run commands
- Download latest from https://gluonhq.com/products/javafx/

## Notes

- The application uses socket communication for scalability
- Each client runs in a separate server thread
- Roles determine UI visibility (Guest ≠ Owner ≠ Admin features)
- Demo data is seeded on first server startup