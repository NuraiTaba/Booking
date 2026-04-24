package interfaces;

public interface IBookingService {
    String createBooking(int userId, String hotel, String date);
    String getUserBookings(int userId);
}