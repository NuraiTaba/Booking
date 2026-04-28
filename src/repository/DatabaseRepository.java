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


    public Integer login(String username, String password) {
    String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
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

    public void saveBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, hotel, booking_date) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setString(2, booking.getHotel());
            stmt.setString(3, booking.getDate());
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

    public boolean register(String username, String password) {
    if (username == null || username.trim().isEmpty() ||
        password == null || password.trim().isEmpty()) {
        return false;
        }
    
    String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        int rows = stmt.executeUpdate();
        return rows > 0;
        } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }
        // ========== ОТЕЛИ ==========
    
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE status = 'ACTIVE'";
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
    
    public List<Hotel> searchHotels(String city) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE city LIKE ? AND status = 'ACTIVE'";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + city + "%");
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
    String sql = "INSERT INTO hotels (name, city, address, stars, price, description, owner_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, name);
        stmt.setString(2, city);
        stmt.setString(3, address);
        stmt.setInt(4, stars);
        stmt.setInt(5, price);
        stmt.setString(6, description);
        stmt.setInt(7, ownerId);
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
            stmt.setInt(1, hotelId);
            stmt.setInt(2, userId);
            stmt.setInt(3, bookingId);
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

    public List<Hotel> filterHotels(int minPrice, int maxPrice, int stars) {
    List<Hotel> hotels = new ArrayList<>();
    String sql = "SELECT * FROM hotels WHERE price BETWEEN ? AND ? AND stars >= ? AND status = 'ACTIVE'";
    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, minPrice);
        stmt.setInt(2, maxPrice);
        stmt.setInt(3, stars);
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