package repository;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Hotel;
import model.Review;

public class DatabaseRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/booking_system";
    private static final String USER = "root";
    private static final String PASSWORD = "@LinDu098"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void initializeProjectData() {
        ensureSchema();
        seedDemoData();
    }

    private void ensureSchema() {
        createBaseTablesIfMissing();
        addColumnIfMissing("hotels", "property_type", "VARCHAR(30) DEFAULT 'Hotel'");
        addColumnIfMissing("hotels", "district", "VARCHAR(80) DEFAULT ''");
        addColumnIfMissing("hotels", "distance_to_center", "DOUBLE DEFAULT 1.0");
        addColumnIfMissing("hotels", "popularity", "INT DEFAULT 0");
        addColumnIfMissing("hotels", "available_rooms", "INT DEFAULT 5");
        addColumnIfMissing("hotels", "has_wifi", "BOOLEAN DEFAULT TRUE");
        addColumnIfMissing("hotels", "has_parking", "BOOLEAN DEFAULT FALSE");
        addColumnIfMissing("hotels", "has_pool", "BOOLEAN DEFAULT FALSE");
        addColumnIfMissing("hotels", "breakfast_included", "BOOLEAN DEFAULT FALSE");
        addColumnIfMissing("hotels", "free_cancellation", "BOOLEAN DEFAULT TRUE");
        addColumnIfMissing("hotels", "gallery_urls", "TEXT");
        addColumnIfMissing("hotels", "policies", "TEXT");
        addColumnIfMissing("hotels", "room_options", "TEXT");
        addColumnIfMissing("hotels", "contact_email", "VARCHAR(120) DEFAULT 'hotel@example.com'");
        addColumnIfMissing("reviews", "cleanliness", "INT DEFAULT 5");
        addColumnIfMissing("reviews", "comfort", "INT DEFAULT 5");
        addColumnIfMissing("reviews", "staff", "INT DEFAULT 5");
        addColumnIfMissing("reviews", "photo_url", "VARCHAR(500) DEFAULT ''");
        addColumnIfMissing("bookings", "guest_name", "VARCHAR(100) DEFAULT ''");
        addColumnIfMissing("bookings", "guest_email", "VARCHAR(120) DEFAULT ''");
        addColumnIfMissing("bookings", "guest_phone", "VARCHAR(40) DEFAULT ''");
        addColumnIfMissing("bookings", "check_out", "VARCHAR(20) DEFAULT ''");
        addColumnIfMissing("bookings", "guests", "INT DEFAULT 1");
        addColumnIfMissing("bookings", "rooms", "INT DEFAULT 1");
        addColumnIfMissing("bookings", "status", "VARCHAR(30) DEFAULT 'CONFIRMED'");
        addColumnIfMissing("users", "full_name", "VARCHAR(100) DEFAULT ''");
        addColumnIfMissing("users", "email", "VARCHAR(120) DEFAULT ''");
        addColumnIfMissing("users", "phone", "VARCHAR(40) DEFAULT ''");
        createWishlistTableIfMissing();
        createRecentSearchesTableIfMissing();
    }

    private void createBaseTablesIfMissing() {
        createUsersTableIfMissing();
        createHotelsTableIfMissing();
        createBookingsTableIfMissing();
        createReviewsTableIfMissing();
    }

    private void createUsersTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "username VARCHAR(50) NOT NULL UNIQUE, " +
                     "password VARCHAR(100) NOT NULL, " +
                     "role ENUM('GUEST','OWNER','ADMIN') DEFAULT 'GUEST', " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create users table: " + e.getMessage());
        }
    }

    private void createHotelsTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS hotels (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "name VARCHAR(150) NOT NULL, " +
                     "city VARCHAR(100) NOT NULL, " +
                     "address VARCHAR(200) DEFAULT '', " +
                     "stars INT DEFAULT 3, " +
                     "price INT DEFAULT 0, " +
                     "rating DOUBLE DEFAULT 0.0, " +
                     "description TEXT, " +
                     "image_url VARCHAR(500), " +
                     "owner_id INT, " +
                     "status VARCHAR(30) DEFAULT 'ACTIVE', " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create hotels table: " + e.getMessage());
        }
    }

    private void createBookingsTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS bookings (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "user_id INT NOT NULL, " +
                     "hotel VARCHAR(150) NOT NULL, " +
                     "booking_date VARCHAR(20) NOT NULL, " +
                     "status VARCHAR(30) DEFAULT 'CONFIRMED', " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create bookings table: " + e.getMessage());
        }
    }

    private void createReviewsTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS reviews (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "hotel_id INT NOT NULL, " +
                     "hotel_name VARCHAR(150) NOT NULL, " +
                     "city VARCHAR(100) NOT NULL, " +
                     "user_id INT NOT NULL, " +
                     "rating INT DEFAULT 5, " +
                     "comment TEXT, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create reviews table: " + e.getMessage());
        }
    }


    private void addColumnIfMissing(String table, String column, String definition) {
        String sql = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (!message.contains("duplicate") && !message.contains("exists")) {
                System.err.println("Could not add column " + table + "." + column + ": " + e.getMessage());
            }
        }
    }

    private void createWishlistTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS wishlists (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "user_id INT NOT NULL, " +
                     "hotel_id INT NOT NULL, " +
                     "collection_name VARCHAR(80) DEFAULT 'Favorites', " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "UNIQUE KEY unique_wishlist_item (user_id, hotel_id, collection_name))";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create wishlists table: " + e.getMessage());
        }
    }

    private void createRecentSearchesTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS recent_searches (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "user_id INT NOT NULL, " +
                     "query_text VARCHAR(200) NOT NULL, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create recent_searches table: " + e.getMessage());
        }
    }


    public Integer login(String username, String password) {
        String sql = "SELECT id, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int roleStringToInt(String role) {
        if (role == null) {
            return 0;
        }
        switch (role.toUpperCase()) {
            case "OWNER":
                return 1;
            case "ADMIN":
                return 2;
            default:
                return 0;
        }
    }

    private static String roleIntToString(int role) {
        switch (role) {
            case 1:
                return "OWNER";
            case 2:
                return "ADMIN";
            default:
                return "GUEST";
        }
    }

    public void saveBooking(Booking booking) {
    // Сначала проверим, нет ли уже такой брони
    String checkSql = "SELECT id FROM bookings WHERE user_id = ? AND hotel = ? AND booking_date = ?";
    try (Connection conn = getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        
        checkStmt.setInt(1, booking.getUserId());
        checkStmt.setString(2, booking.getHotel());
        checkStmt.setString(3, booking.getDate());
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            System.out.println("⚠️ Бронь уже существует, пропускаем");
            return; // Не создаем дубликат
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Если брони нет - вставляем
    String sql = "INSERT INTO bookings (user_id, hotel, booking_date) VALUES (?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, booking.getUserId());
        stmt.setString(2, booking.getHotel());
        stmt.setString(3, booking.getDate());
        stmt.executeUpdate();
        System.out.println("✅ Бронь сохранена");
    } catch (SQLException e) {
        System.err.println("❌ Ошибка сохранения: " + e.getMessage());
        e.printStackTrace();
    }
}

    public boolean saveFullBooking(int userId, String hotel, String checkIn, String checkOut,
                                   int guests, int rooms, String guestName, String email, String phone) {
    String sql = "INSERT INTO bookings (user_id, hotel, booking_date, check_out, guests, rooms, guest_name, guest_email, guest_phone, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.setString(2, hotel);
        stmt.setString(3, checkIn);
        stmt.setString(4, checkOut);
        stmt.setInt(5, guests);
        stmt.setInt(6, rooms);
        stmt.setString(7, guestName);
        stmt.setString(8, email);
        stmt.setString(9, phone);
        stmt.executeUpdate();
        incrementHotelPopularity(hotel);
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private void incrementHotelPopularity(String hotel) {
    String sql = "UPDATE hotels SET popularity = popularity + 1, available_rooms = GREATEST(available_rooms - 1, 0) WHERE name = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, hotel);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("user_id"),
                    rs.getString("hotel"),
                    rs.getString("booking_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public String getBookingSummaryByUser(int userId) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append("Booking ID: ").append(rs.getInt("id")).append("\n")
                  .append("Hotel: ").append(rs.getString("hotel")).append("\n")
                  .append("Dates: ").append(rs.getString("booking_date"));
                String checkOut = rs.getString("check_out");
                if (checkOut != null && !checkOut.trim().isEmpty()) {
                    sb.append(" - ").append(checkOut);
                }
                sb.append("\n")
                  .append("Guests: ").append(rs.getInt("guests")).append(", rooms: ").append(rs.getInt("rooms")).append("\n")
                  .append("Guest: ").append(rs.getString("guest_name")).append("\n")
                  .append("Contact: ").append(rs.getString("guest_email")).append(" / ").append(rs.getString("guest_phone")).append("\n")
                  .append("Status: ").append(rs.getString("status")).append("\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No bookings found";
    }

    public boolean cancelBooking(int userId, int bookingId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeBookingDates(int userId, int bookingId, String checkIn, String checkOut) {
        String sql = "UPDATE bookings SET booking_date = ?, check_out = ?, status = 'CONFIRMED' WHERE id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, checkIn);
            stmt.setString(2, checkOut);
            stmt.setInt(3, bookingId);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addToWishlist(int userId, int hotelId, String collectionName) {
        String sql = "INSERT IGNORE INTO wishlists (user_id, hotel_id, collection_name) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, hotelId);
            stmt.setString(3, collectionName == null || collectionName.trim().isEmpty() ? "Favorites" : collectionName.trim());
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFromWishlist(int userId, int hotelId) {
        String sql = "DELETE FROM wishlists WHERE user_id = ? AND hotel_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, hotelId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getWishlist(int userId) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT w.collection_name, h.* FROM wishlists w JOIN hotels h ON w.hotel_id = h.id " +
                     "WHERE w.user_id = ? ORDER BY w.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("|")
                  .append(rs.getString("collection_name")).append("|")
                  .append(rs.getString("name")).append("|")
                  .append(rs.getString("city")).append("|")
                  .append(rs.getInt("stars")).append("|")
                  .append(rs.getInt("price")).append("|")
                  .append(rs.getDouble("rating")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY Wishlist is empty";
    }

    public String getUserProfile(int userId) {
        String sql = "SELECT username, full_name, email, phone FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username") + "|" +
                       rs.getString("full_name") + "|" +
                       rs.getString("email") + "|" +
                       rs.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ERROR Profile not found";
    }

    public boolean updateUserProfile(int userId, String fullName, String email, String phone) {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getUserRole(int userId) {
        String sql = "SELECT role FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return roleStringToInt(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getAllUsers() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT id, username, role, full_name, email FROM users ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("|")
                  .append(rs.getString("username")).append("|")
                  .append(rs.getString("role")).append("|")
                  .append(rs.getString("full_name")).append("|")
                  .append(rs.getString("email")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "No users found";
    }

    public String getAllHotelsAdmin() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT id, name, city, owner_id, rating, status FROM hotels ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("|")
                  .append(rs.getString("name")).append("|")
                  .append(rs.getString("city")).append("|")
                  .append(rs.getInt("owner_id")).append("|")
                  .append(rs.getDouble("rating")).append("|")
                  .append(rs.getString("status")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "No hotels found";
    }

    public String getStats() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = getConnection()) {
            // Total users
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) sb.append("Total users: ").append(rs.getInt(1)).append("\n");
            }
            // Total hotels
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM hotels")) {
                if (rs.next()) sb.append("Total hotels: ").append(rs.getInt(1)).append("\n");
            }
            // Total bookings
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings")) {
                if (rs.next()) sb.append("Total bookings: ").append(rs.getInt(1)).append("\n");
            }
            // Total reviews
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM reviews")) {
                if (rs.next()) sb.append("Total reviews: ").append(rs.getInt(1)).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
        
    // Запустите один раз, чтобы почистить существующие дубликаты
public void cleanupDuplicates() {
    String sql = "DELETE b1 FROM bookings b1 " +
                 "INNER JOIN bookings b2 " +
                 "WHERE b1.id > b2.id AND b1.user_id = b2.user_id " +
                 "AND b1.hotel = b2.hotel AND b1.booking_date = b2.booking_date";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        int deleted = stmt.executeUpdate(sql);
        System.out.println("Удалено дубликатов: " + deleted);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    // ========== ОТЕЛИ ==========
    
    public List<Hotel> getAllHotels() {
        return getHotelsSorted("rating");
    }

    public List<Hotel> getHotelsSorted(String sortBy) {
        List<Hotel> hotels = new ArrayList<>();
        String orderBy = "rating DESC";
        if ("price".equalsIgnoreCase(sortBy)) {
            orderBy = "price ASC";
        } else if ("popularity".equalsIgnoreCase(sortBy)) {
            orderBy = "popularity DESC";
        }
        String sql = "SELECT * FROM hotels WHERE status = 'ACTIVE' ORDER BY " + orderBy;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hotels.add(new Hotel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getInt("stars"),
                    rs.getInt("price"),
                    rs.getDouble("rating"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getInt("owner_id"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }
    
    public List<Hotel> searchHotels(String query) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE status = 'ACTIVE' AND " +
                     "(name LIKE ? OR city LIKE ? OR address LIKE ? OR description LIKE ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String search = "%" + query + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hotels.add(new Hotel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getInt("stars"),
                    rs.getInt("price"),
                    rs.getDouble("rating"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getInt("owner_id"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    public void saveRecentSearch(int userId, String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        String deleteSql = "DELETE FROM recent_searches WHERE user_id = ? AND query_text = ?";
        String insertSql = "INSERT INTO recent_searches (user_id, query_text) VALUES (?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.setString(2, query.trim());
                deleteStmt.executeUpdate();
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, query.trim());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getRecentSearches(int userId) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT query_text FROM recent_searches WHERE user_id = ? ORDER BY created_at DESC LIMIT 8";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("query_text")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No recent searches";
    }

    public String getPopularDestinations() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT city, COUNT(*) AS hotel_count, AVG(rating) AS avg_rating, MIN(price) AS min_price " +
                     "FROM hotels WHERE status = 'ACTIVE' GROUP BY city ORDER BY hotel_count DESC, avg_rating DESC LIMIT 8";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sb.append(rs.getString("city")).append("|")
                  .append(rs.getInt("hotel_count")).append("|")
                  .append(rs.getDouble("avg_rating")).append("|")
                  .append(rs.getInt("min_price")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No destinations";
    }

    public String getAutocompleteSuggestions(String query) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT DISTINCT name AS suggestion FROM hotels WHERE status = 'ACTIVE' AND name LIKE ? " +
                     "UNION SELECT DISTINCT city AS suggestion FROM hotels WHERE status = 'ACTIVE' AND city LIKE ? " +
                     "UNION SELECT DISTINCT address AS suggestion FROM hotels WHERE status = 'ACTIVE' AND address LIKE ? " +
                     "LIMIT 10";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String search = "%" + (query == null ? "" : query.trim()) + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("suggestion")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No suggestions";
    }

    public boolean register(String username, String password, int role) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }
        
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, roleIntToString(role));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void seedDemoData() {
        int demoOwnerId = ensureDemoOwner();
        int demoGuestId = ensureDemoGuest();
        int demoAdminId = ensureDemoAdmin();
        seedHotel("Rixos Almaty", "Almaty", "Seyfullin Avenue 506/99", 5, 85000,
                "Luxury hotel in the center of Almaty with spa, breakfast, Wi-Fi and business facilities.",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=900",
                demoOwnerId);
        updateHotelExtras("Rixos Almaty", "Almaty", "Hotel", "Bostandyk", 0.8, 95, 8, true, true, true, true, true);
        updateHotelContent("Rixos Almaty", "Almaty",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=900,https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=900,https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=900",
                "Check-in: 14:00; Check-out: 12:00; Free cancellation until 24 hours before arrival; No smoking rooms available.",
                "Standard Room - 85000 KZT - breakfast included; Deluxe Room - 115000 KZT - spa access; Suite - 180000 KZT - city view and lounge access",
                "rixos.almaty@example.com");
        seedHotel("Rixos President Astana", "Astana", "D. Kunayev Street 7", 5, 90000,
                "Premium hotel for business and leisure guests near government and business districts.",
                "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=900",
                demoOwnerId);
        updateHotelExtras("Rixos President Astana", "Astana", "Hotel", "Left Bank", 1.5, 88, 6, true, true, false, true, true);
        updateHotelContent("Rixos President Astana", "Astana",
                "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=900,https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=900",
                "Check-in: 15:00; Check-out: 12:00; Free cancellation for flexible rates; Pets on request.",
                "Business Room - 90000 KZT - work desk; Premium Room - 130000 KZT - breakfast and parking; Presidential Suite - 250000 KZT - VIP service",
                "rixos.astana@example.com");
        seedHotel("Kazakhstan Hotel", "Almaty", "Dostyk Avenue 52/2", 4, 45000,
                "Classic city hotel close to restaurants, parks and popular attractions.",
                "https://images.unsplash.com/photo-1455587734955-081b22074882?w=900",
                demoOwnerId);
        updateHotelExtras("Kazakhstan Hotel", "Almaty", "Hotel", "Medeu", 1.2, 73, 12, true, true, false, false, true);
        updateHotelContent("Kazakhstan Hotel", "Almaty",
                "https://images.unsplash.com/photo-1455587734955-081b22074882?w=900,https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=900",
                "Check-in: 14:00; Check-out: 12:00; Free cancellation before arrival day.",
                "Economy Room - 45000 KZT - city view; Standard Room - 55000 KZT - breakfast optional; Family Room - 72000 KZT - two beds",
                "kazakhstan.hotel@example.com");

        seedReview("Rixos Almaty", "Almaty", demoGuestId, 5, "Excellent location, clean rooms and very helpful staff.");
        seedReview("Rixos Almaty", "Almaty", demoGuestId, 4, "Great breakfast and spa. The room was comfortable.");
        seedReview("Rixos President Astana", "Astana", demoGuestId, 5, "Perfect for a business trip, quiet and professional service.");
    }

    private int ensureDemoOwner() {
        return ensureUser("demo_owner", "demo", 1); // Owner
    }

    private int ensureDemoGuest() {
        return ensureUser("demo_guest", "demo", 0); // Guest
    }

    private int ensureDemoAdmin() {
        return ensureUser("admin", "admin", 2); // Admin
    }

    private int ensureUser(String username, String password, int role) {
        String selectSql = "SELECT id, role FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        String updateSql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, username);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String existingRole = rs.getString("role");
                    if (roleStringToInt(existingRole) != role) {
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, roleIntToString(role));
                            updateStmt.setInt(2, userId);
                            updateStmt.executeUpdate();
                        }
                    }
                    return userId;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, roleIntToString(role));
                insertStmt.executeUpdate();
                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Could not create demo user: " + e.getMessage());
        }
        return 1;
    }

    private void seedReview(String hotelName, String city, int userId, int rating, String comment) {
        Integer hotelId = findHotelId(hotelName, city);
        if (hotelId == null) {
            return;
        }

        String checkSql = "SELECT id FROM reviews WHERE hotel_id = ? AND user_id = ? AND comment = ?";
        String insertSql = "INSERT INTO reviews (hotel_id, user_id, booking_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, hotelId);
            checkStmt.setInt(2, userId);
            checkStmt.setString(3, comment);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return;
            }

            int bookingId = ensureDemoBooking(conn, userId, hotelName);
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, hotelId);
                insertStmt.setInt(2, userId);
                insertStmt.setInt(3, bookingId);
                insertStmt.setInt(4, rating);
                insertStmt.setString(5, comment);
                insertStmt.executeUpdate();
                updateHotelRating(hotelId);
            }
        } catch (SQLException e) {
            System.err.println("Could not seed review for " + hotelName + ": " + e.getMessage());
        }
    }

    private Integer findHotelId(String hotelName, String city) {
        String sql = "SELECT id FROM hotels WHERE name = ? AND city = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hotelName);
            stmt.setString(2, city);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Could not find hotel " + hotelName + ": " + e.getMessage());
        }
        return null;
    }

    private int ensureDemoBooking(Connection conn, int userId, String hotelName) throws SQLException {
        String date = "2026-05-01";
        String selectSql = "SELECT id FROM bookings WHERE user_id = ? AND hotel = ? AND booking_date = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, userId);
            selectStmt.setString(2, hotelName);
            selectStmt.setString(3, date);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insertSql = "INSERT INTO bookings (user_id, hotel, booking_date) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, userId);
            insertStmt.setString(2, hotelName);
            insertStmt.setString(3, date);
            insertStmt.executeUpdate();
            ResultSet keys = insertStmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return 0;
    }

    private void seedHotel(String name, String city, String address, int stars, int price,
                           String description, String imageUrl, int ownerId) {
        String sql = "INSERT INTO hotels (name, city, address, stars, price, rating, description, image_url, owner_id, status) " +
                     "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE' " +
                     "WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name = ? AND city = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, city);
            stmt.setString(3, address);
            stmt.setInt(4, stars);
            stmt.setInt(5, price);
            stmt.setDouble(6, 4.7);
            stmt.setString(7, description);
            stmt.setString(8, imageUrl);
            stmt.setInt(9, ownerId);
            stmt.setString(10, name);
            stmt.setString(11, city);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not seed demo hotel " + name + ": " + e.getMessage());
        }
    }

    private void updateHotelExtras(String name, String city, String propertyType, String district,
                                   double distance, int popularity, int availableRooms,
                                   boolean wifi, boolean parking, boolean pool,
                                   boolean breakfast, boolean freeCancellation) {
        String sql = "UPDATE hotels SET property_type=?, district=?, distance_to_center=?, popularity=?, " +
                     "available_rooms=?, has_wifi=?, has_parking=?, has_pool=?, breakfast_included=?, " +
                     "free_cancellation=? WHERE name=? AND city=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, propertyType);
            stmt.setString(2, district);
            stmt.setDouble(3, distance);
            stmt.setInt(4, popularity);
            stmt.setInt(5, availableRooms);
            stmt.setBoolean(6, wifi);
            stmt.setBoolean(7, parking);
            stmt.setBoolean(8, pool);
            stmt.setBoolean(9, breakfast);
            stmt.setBoolean(10, freeCancellation);
            stmt.setString(11, name);
            stmt.setString(12, city);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not update extras for " + name + ": " + e.getMessage());
        }
    }

    public void updateHotelExtrasById(int hotelId, String propertyType, String district,
                                      double distance, int popularity, int availableRooms,
                                      boolean wifi, boolean parking, boolean pool,
                                      boolean breakfast, boolean freeCancellation) {
        String sql = "UPDATE hotels SET property_type=?, district=?, distance_to_center=?, popularity=?, " +
                     "available_rooms=?, has_wifi=?, has_parking=?, has_pool=?, breakfast_included=?, " +
                     "free_cancellation=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, propertyType);
            stmt.setString(2, district);
            stmt.setDouble(3, distance);
            stmt.setInt(4, popularity);
            stmt.setInt(5, availableRooms);
            stmt.setBoolean(6, wifi);
            stmt.setBoolean(7, parking);
            stmt.setBoolean(8, pool);
            stmt.setBoolean(9, breakfast);
            stmt.setBoolean(10, freeCancellation);
            stmt.setInt(11, hotelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not update hotel extras: " + e.getMessage());
        }
    }

    private void updateHotelContent(String name, String city, String galleryUrls, String policies,
                                    String roomOptions, String contactEmail) {
        String sql = "UPDATE hotels SET gallery_urls=?, policies=?, room_options=?, contact_email=? WHERE name=? AND city=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, galleryUrls);
            stmt.setString(2, policies);
            stmt.setString(3, roomOptions);
            stmt.setString(4, contactEmail);
            stmt.setString(5, name);
            stmt.setString(6, city);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not update hotel content for " + name + ": " + e.getMessage());
        }
    }

    public String getHotelContent(int hotelId) {
        String sql = "SELECT gallery_urls, policies, room_options, contact_email FROM hotels WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return safe(rs.getString("gallery_urls")) + "|" +
                       safe(rs.getString("policies")) + "|" +
                       safe(rs.getString("room_options")) + "|" +
                       safe(rs.getString("contact_email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "|||hotel@example.com";
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("|", "/");
    }
    
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Hotel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getInt("stars"),
                    rs.getInt("price"),
                    rs.getDouble("rating"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getInt("owner_id"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Hotel> getHotelsByOwner(int ownerId) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hotels.add(new Hotel(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getInt("stars"),
                    rs.getInt("price"),
                    rs.getDouble("rating"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getInt("owner_id"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }
    public int addHotel(String name, String city, String address, int stars, int price, String description, int ownerId) {
    return addHotel(name, city, address, stars, price, description, "", ownerId);
}

    public int addHotel(String name, String city, String address, int stars, int price, String description, String imageUrl, int ownerId) {
    String sql = "INSERT INTO hotels (name, city, address, stars, price, description, image_url, owner_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, name);
        stmt.setString(2, city);
        stmt.setString(3, address);
        stmt.setInt(4, stars);
        stmt.setInt(5, price);
        stmt.setString(6, description);
        stmt.setString(7, imageUrl);
        stmt.setInt(8, ownerId);
        stmt.executeUpdate();
        
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}
    
    // ========== ОТЗЫВЫ ==========
    
    public boolean addReview(int hotelId, int userId, int bookingId, int rating, String comment) {
        String sql = "INSERT INTO reviews (hotel_id, user_id, booking_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int actualBookingId = bookingId > 0 ? bookingId : ensureBookingForReview(conn, userId, hotelId);
            stmt.setInt(1, hotelId);
            stmt.setInt(2, userId);
            stmt.setInt(3, actualBookingId);
            stmt.setInt(4, rating);
            stmt.setString(5, comment);
            stmt.executeUpdate();
            updateHotelRating(hotelId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int ensureBookingForReview(Connection conn, int userId, int hotelId) throws SQLException {
        Hotel hotel = getHotelById(hotelId);
        String hotelName = hotel != null ? hotel.getName() : "Hotel #" + hotelId;
        return ensureDemoBooking(conn, userId, hotelName);
    }
    
    private void updateHotelRating(int hotelId) {
        String sql = "UPDATE hotels SET rating = (SELECT AVG(rating) FROM reviews WHERE hotel_id = ?) WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            stmt.setInt(2, hotelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Review> getReviewsByHotel(int hotelId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.hotel_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Review(
                    rs.getInt("id"),
                    rs.getInt("hotel_id"),
                    rs.getInt("user_id"),
                    rs.getInt("booking_id"),
                    rs.getInt("rating"),
                    rs.getString("comment") + " — " + rs.getString("username"),
                    rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public String getReviewsByHotelSorted(int hotelId, String sortBy, int minRating) {
        String orderBy = "r.created_at DESC";
        if ("rating".equalsIgnoreCase(sortBy)) {
            orderBy = "r.rating DESC";
        } else if ("cleanliness".equalsIgnoreCase(sortBy)) {
            orderBy = "r.cleanliness DESC";
        } else if ("comfort".equalsIgnoreCase(sortBy)) {
            orderBy = "r.comfort DESC";
        } else if ("staff".equalsIgnoreCase(sortBy)) {
            orderBy = "r.staff DESC";
        }
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT r.*, u.username FROM reviews r JOIN users u ON r.user_id = u.id " +
                     "WHERE r.hotel_id = ? AND r.rating >= ? ORDER BY " + orderBy;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            stmt.setInt(2, minRating);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getInt("rating")).append("*|")
                  .append("Cleanliness ").append(rs.getInt("cleanliness")).append(", ")
                  .append("Comfort ").append(rs.getInt("comfort")).append(", ")
                  .append("Staff ").append(rs.getInt("staff")).append("|")
                  .append(rs.getString("comment")).append(" - ").append(rs.getString("username")).append("|")
                  .append(safe(rs.getString("photo_url"))).append("|")
                  .append(rs.getString("created_at")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No reviews yet";
    }

    public boolean addDetailedReview(int hotelId, int userId, int bookingId, int rating, int cleanliness,
                                     int comfort, int staff, String comment, String photoUrl) {
        String sql = "INSERT INTO reviews (hotel_id, user_id, booking_id, rating, cleanliness, comfort, staff, comment, photo_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int actualBookingId = bookingId > 0 ? bookingId : ensureBookingForReview(conn, userId, hotelId);
            stmt.setInt(1, hotelId);
            stmt.setInt(2, userId);
            stmt.setInt(3, actualBookingId);
            stmt.setInt(4, rating);
            stmt.setInt(5, cleanliness);
            stmt.setInt(6, comfort);
            stmt.setInt(7, staff);
            stmt.setString(8, comment);
            stmt.setString(9, photoUrl);
            stmt.executeUpdate();
            updateHotelRating(hotelId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRecommendations(int userId) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT DISTINCT h.* FROM hotels h " +
                     "LEFT JOIN bookings b ON b.user_id = ? " +
                     "WHERE h.status = 'ACTIVE' AND (h.city = (SELECT hotel_city.city FROM hotels hotel_city " +
                     "JOIN bookings user_booking ON user_booking.hotel = hotel_city.name WHERE user_booking.user_id = ? LIMIT 1) " +
                     "OR h.rating >= 4.5) ORDER BY h.rating DESC, h.popularity DESC LIMIT 6";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("|")
                  .append(rs.getString("name")).append("|")
                  .append(rs.getString("city")).append("|")
                  .append(rs.getInt("stars")).append("|")
                  .append(rs.getInt("price")).append("|")
                  .append(rs.getDouble("rating")).append("|")
                  .append(rs.getString("image_url")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (sb.length() == 0) {
            List<Hotel> hotels = getHotelsSorted("rating");
            for (int i = 0; i < Math.min(6, hotels.size()); i++) {
                Hotel h = hotels.get(i);
                sb.append(h.getId()).append("|").append(h.getName()).append("|").append(h.getCity()).append("|")
                  .append(h.getStars()).append("|").append(h.getPrice()).append("|").append(h.getRating()).append("|")
                  .append(h.getImageUrl()).append("\n");
            }
        }
        return sb.length() > 0 ? sb.toString() : "EMPTY No recommendations";
    }

    public String contactHotel(int userId, int hotelId, String message) {
        Hotel hotel = getHotelById(hotelId);
        if (hotel == null) {
            return "ERROR Hotel not found";
        }
        String sql = "SELECT contact_email FROM hotels WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            String email = rs.next() ? rs.getString("contact_email") : "hotel@example.com";
            return "SUCCESS Message sent to " + email + ": " + message;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ERROR Failed to contact hotel";
    }

    public List<Hotel> filterHotels(int minPrice, int maxPrice, int stars) {
        return filterHotels(minPrice, maxPrice, stars, "Any", false, false, false, false, false, "", false, "rating");
    }

    public List<Hotel> filterHotels(int minPrice, int maxPrice, int stars, String propertyType,
                                    boolean wifi, boolean parking, boolean pool, boolean breakfast,
                                    boolean freeCancellation, String district, boolean availableOnly,
                                    String sortBy) {
    List<Hotel> hotels = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM hotels WHERE price BETWEEN ? AND ? AND stars >= ? AND status = 'ACTIVE'");
    List<Object> params = new ArrayList<>();
    params.add(minPrice);
    params.add(maxPrice);
    params.add(stars);

    if (propertyType != null && !propertyType.equalsIgnoreCase("Any")) {
        sql.append(" AND property_type = ?");
        params.add(propertyType);
    }
    if (wifi) {
        sql.append(" AND has_wifi = TRUE");
    }
    if (parking) {
        sql.append(" AND has_parking = TRUE");
    }
    if (pool) {
        sql.append(" AND has_pool = TRUE");
    }
    if (breakfast) {
        sql.append(" AND breakfast_included = TRUE");
    }
    if (freeCancellation) {
        sql.append(" AND free_cancellation = TRUE");
    }
    if (district != null && !district.trim().isEmpty()) {
        sql.append(" AND district LIKE ?");
        params.add("%" + district.trim() + "%");
    }
    if (availableOnly) {
        sql.append(" AND available_rooms > 0");
    }

    if ("price".equalsIgnoreCase(sortBy)) {
        sql.append(" ORDER BY price ASC");
    } else if ("popularity".equalsIgnoreCase(sortBy)) {
        sql.append(" ORDER BY popularity DESC");
    } else {
        sql.append(" ORDER BY rating DESC");
    }

    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            } else {
                stmt.setString(i + 1, String.valueOf(param));
            }
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            hotels.add(new Hotel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("city"),
                rs.getString("address"),
                rs.getInt("stars"),
                rs.getInt("price"),
                rs.getDouble("rating"),
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getInt("owner_id"),
                rs.getString("status")
            ));
        }
        } catch (SQLException e) {
            e.printStackTrace();
            }
            return hotels;
    }

    public String getHotelExtras(int hotelId) {
    String sql = "SELECT property_type, district, distance_to_center, popularity, available_rooms, " +
                 "has_wifi, has_parking, has_pool, breakfast_included, free_cancellation " +
                 "FROM hotels WHERE id = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, hotelId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("property_type") + "|" +
                   rs.getString("district") + "|" +
                   rs.getDouble("distance_to_center") + "|" +
                   rs.getInt("popularity") + "|" +
                   rs.getInt("available_rooms") + "|" +
                   rs.getBoolean("has_wifi") + "|" +
                   rs.getBoolean("has_parking") + "|" +
                   rs.getBoolean("has_pool") + "|" +
                   rs.getBoolean("breakfast_included") + "|" +
                   rs.getBoolean("free_cancellation");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Hotel||1.0|0|0|false|false|false|false|false";
    }
    public boolean updateHotel(int hotelId, String name, String city, String address, int stars, int price, String description) {
    String sql = "UPDATE hotels SET name=?, city=?, address=?, stars=?, price=?, description=? WHERE id=?";
    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, name);
        stmt.setString(2, city);
        stmt.setString(3, address);
        stmt.setInt(4, stars);
        stmt.setInt(5, price);
        stmt.setString(6, description);
        stmt.setInt(7, hotelId);
        int rows = stmt.executeUpdate();
        return rows > 0;
        } catch (SQLException e) {
        e.printStackTrace();
        }
    return false;
    }
    public String getOwnerBookings(int ownerId) {
    StringBuilder sb = new StringBuilder();
    String sql = "SELECT b.*, u.username FROM bookings b " +
                 "JOIN hotels h ON b.hotel = h.name " +
                 "JOIN users u ON b.user_id = u.id " +
                 "WHERE h.owner_id = ? ORDER BY b.booking_date DESC";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, ownerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            sb.append(rs.getString("username")).append("|")
              .append(rs.getString("hotel")).append("|")
              .append(rs.getString("booking_date")).append("\n");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sb.length() > 0 ? sb.toString() : "EMPTY No bookings";
}
}
