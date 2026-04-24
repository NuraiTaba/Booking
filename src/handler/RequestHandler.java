package handler;

import service.AuthService;
import service.BookingService;

public class RequestHandler {
    private AuthService authService;
    private BookingService bookingService;

    public RequestHandler(AuthService authService, BookingService bookingService) {
        this.authService = authService;
        this.bookingService = bookingService;
    }

    public String handle(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 0) {
            return "ERROR Empty command";
        }

        String command = parts[0];

        switch (command) {
            case "LOGIN":
                if (parts.length == 3) {
                    return authService.login(parts[1], parts[2]);
                }
                return "ERROR Usage: LOGIN username password";

            case "BOOK":
                if (parts.length == 4) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        return bookingService.createBooking(userId, parts[2], parts[3]);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: BOOK userId hotel date";

            case "GET_BOOKINGS":
                if (parts.length == 2) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        return bookingService.getUserBookings(userId);
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