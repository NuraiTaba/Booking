package service;

import model.Booking;
import repository.DatabaseRepository;
import interfaces.IBookingService;
import java.util.List;

public class BookingService implements IBookingService {
    private DatabaseRepository repo;

    public BookingService(DatabaseRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public String createBooking(int userId, String hotel, String date) {
        if (hotel == null || hotel.trim().isEmpty()) {
            return "ERROR Hotel name cannot be empty";
        }
        if (date == null || date.trim().isEmpty()) {
            return "ERROR Date cannot be empty";
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "ERROR Date format must be YYYY-MM-DD";
        }
        
        Booking booking = new Booking(userId, hotel, date);
        repo.saveBooking(booking);
        return "SUCCESS Booking created";
    }

    @Override
    public String getUserBookings(int userId) {
        return repo.getBookingSummaryByUser(userId);
    }
}
