package handler;

import service.AuthService;
import service.BookingService;
import util.FileLogger;  

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
        String response;

        switch (command){
            case "LOGIN":
                if (parts.length == 3) {
                    response = authService.login(parts[1], parts[2]);
                    FileLogger.log(request, response);              
                    return response;
                }
                response = "ERROR Usage: LOGIN username password";
                FileLogger.log(request, response);  
                return response;

            case "REGISTER":
                if (parts.length == 3) {
                    response = authService.register(parts[1], parts[2]);
                    FileLogger.log(request, response);  
                    return response;
                }
                response = "ERROR Usage: REGISTER username password";
                FileLogger.log(request, response);  
                return response;

            case "BOOK":
                if (parts.length == 4) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        response = bookingService.createBooking(userId, parts[2], parts[3]);
                        FileLogger.log(request, response);  
                        return response;
                    } catch (NumberFormatException e) {
                        response = "ERROR Invalid userId";
                        FileLogger.log(request, response);  
                        return response;
                    }
                }
                response = "ERROR Usage: BOOK userId hotel date";
                FileLogger.log(request, response); 
                return response;

            case "GET_BOOKINGS":
                if (parts.length == 2) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        response = bookingService.getUserBookings(userId);
                        FileLogger.log(request, response);  
                        return response;
                    } catch (NumberFormatException e) {
                        response = "ERROR Invalid userId";
                        FileLogger.log(request, response);  
                        return response;
                    }
                }
                response = "ERROR Usage: GET_BOOKINGS userId";
                FileLogger.log(request, response);  
                return response;

            default:
                response = "ERROR Unknown command: " + command;
                FileLogger.log(request, response);  
                return response;
        
        }
    }
}