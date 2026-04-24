package repository;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/booking_system";
    private static final String USER = "root";
    private static final String PASSWORD = "@LinDu098";  // ← ТВОЙ ПАРОЛЬ

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
}