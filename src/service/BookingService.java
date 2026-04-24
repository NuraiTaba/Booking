package service;

import model.Booking;
import repository.DatabaseRepository;
import java.util.List;

public class BookingService {
    private DatabaseRepository repo;

    public BookingService(DatabaseRepository repo) {
        this.repo = repo;
    }

    public String createBooking(int userId, String hotel, String date) {
        Booking booking = new Booking(userId, hotel, date);
        repo.saveBooking(booking);
        return "SUCCESS Booking created";
    }

    public String getUserBookings(int userId) {
        List<Booking> bookings = repo.getBookingsByUser(userId);
        if (bookings.isEmpty()) {
            return "EMPTY No bookings found";
        }
        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) {
            sb.append(b.toString()).append("\n");
        }
        return sb.toString();
    }
}