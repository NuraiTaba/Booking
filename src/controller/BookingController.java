package controller;

import service.AuthService;
import service.BookingService;

public class BookingController {
    private AuthService authService;
    private BookingService bookingService;
    
    public BookingController(AuthService authService, BookingService bookingService) {
        this.authService = authService;
        this.bookingService = bookingService;
    }
    
    public String handleLogin(String username, String password) {
        return authService.login(username, password);
    }
    
    public String handleRegister(String username, String password) {
        return authService.register(username, password);
    }
    
    public String handleBook(int userId, String hotel, String date) {
        return bookingService.createBooking(userId, hotel, date);
    }
    
    public String handleGetBookings(int userId) {
        return bookingService.getUserBookings(userId);
    }
    
    public String processCommand(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 0) {
            return "ERROR Empty command";
        }
        
        String command = parts[0];
        
        switch (command) {
            case "LOGIN":
                if (parts.length == 3) {
                    return handleLogin(parts[1], parts[2]);
                }
                return "ERROR Usage: LOGIN username password";
                
            case "REGISTER":
                if (parts.length == 3) {
                    return handleRegister(parts[1], parts[2]);
                }
                return "ERROR Usage: REGISTER username password";
                
            case "BOOK":
                if (parts.length == 4) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        return handleBook(userId, parts[2], parts[3]);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: BOOK userId hotel date";
                
            case "GET_BOOKINGS":
                if (parts.length == 2) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        return handleGetBookings(userId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: GET_BOOKINGS userId";
                
            default:
                return "ERROR Unknown command: " + command;
        }
    }
}