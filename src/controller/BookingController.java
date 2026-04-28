package controller;

import service.AuthService;
import service.BookingService;
import repository.DatabaseRepository;
import model.Hotel;
import model.Review;
import java.util.List;

public class BookingController {
    private AuthService authService;
    private BookingService bookingService;
    private DatabaseRepository repository;
    
    public BookingController(AuthService authService, BookingService bookingService, DatabaseRepository repository) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.repository = repository;
    }
    
    public String processCommand(String request) {
        String[] parts = request.split(" ");
        if (parts.length == 0) {
            return "ERROR Empty command";
        }
        
        String command = parts[0];
        System.out.println("📥 Received command: " + command);
        switch (command) {
            case "LOGIN":
                if (parts.length == 3) {
                    return authService.login(parts[1], parts[2]);
                }
                return "ERROR Usage: LOGIN username password";
                
            case "REGISTER":
                if (parts.length == 3) {
                    return authService.register(parts[1], parts[2]);
                }
                return "ERROR Usage: REGISTER username password";
                
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
                
            // ========== НОВЫЕ КОМАНДЫ ДЛЯ ОТЕЛЕЙ ==========
            case "GET_HOTELS":
                return handleGetHotels();
                
            case "SEARCH":
                if (parts.length >= 2) {
                    String city = parts[1];
                    return handleSearchHotels(city);
                }
                return "ERROR Usage: SEARCH city";
                
            case "HOTEL_DETAIL":
                if (parts.length == 2) {
                    try {
                        int hotelId = Integer.parseInt(parts[1]);
                        return handleHotelDetail(hotelId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid hotelId";
                    }
                }
                return "ERROR Usage: HOTEL_DETAIL hotelId";
                
            case "ADD_HOTEL":
                if (parts.length >= 7) {
                    try {
                        String name = parts[1];
                        String city = parts[2];
                        String address = parts[3];
                        int stars = Integer.parseInt(parts[4]);
                        int price = Integer.parseInt(parts[5]);
                        String description = parts[6];
                        int ownerId = Integer.parseInt(parts[7]);
                        return handleAddHotel(name, city, address, stars, price, description, ownerId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid number format";
                    }
                }
                return "ERROR Usage: ADD_HOTEL name city address stars price description ownerId";
                
            case "MY_HOTELS":
                if (parts.length == 2) {
                    try {
                        int ownerId = Integer.parseInt(parts[1]);
                        return handleMyHotels(ownerId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid ownerId";
                    }
                }
                return "ERROR Usage: MY_HOTELS ownerId";
                
            // ========== КОМАНДЫ ДЛЯ ОТЗЫВОВ ==========
            case "ADD_REVIEW":
                if (parts.length == 6) {
                    try {
                        int hotelId = Integer.parseInt(parts[1]);
                        int userId = Integer.parseInt(parts[2]);
                        int bookingId = Integer.parseInt(parts[3]);
                        int rating = Integer.parseInt(parts[4]);
                        String comment = parts[5];
                        return handleAddReview(hotelId, userId, bookingId, rating, comment);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid number format";
                    }
                }
                return "ERROR Usage: ADD_REVIEW hotelId userId bookingId rating comment";
                
            case "GET_REVIEWS":
                if (parts.length == 2) {
                    try {
                        int hotelId = Integer.parseInt(parts[1]);
                        return handleGetReviews(hotelId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid hotelId";
                    }
                }
                return "ERROR Usage: GET_REVIEWS hotelId";
                
            default:
                return "ERROR Unknown command: " + command;
        }
    }
    
    // ========== ОБРАБОТЧИКИ ==========
    
    private String handleGetHotels() {

        System.out.println("🔍 handleGetHotels() called");
        List<Hotel> hotels = repository.getAllHotels();
        System.out.println("Hotels found: " + hotels.size());
        if (hotels.isEmpty()) {
            return "EMPTY No hotels found";
        }
        StringBuilder sb = new StringBuilder();
        for (Hotel h : hotels) {
            sb.append(h.getId()).append("|")
              .append(h.getName()).append("|")
              .append(h.getCity()).append("|")
              .append(h.getStars()).append("|")
              .append(h.getPrice()).append("|")
              .append(h.getRating()).append("|")
              .append(h.getImageUrl()).append("\n");
        }
        return sb.toString();
        
    }
    
    private String handleSearchHotels(String city) {
        List<Hotel> hotels = repository.searchHotels(city);
        if (hotels.isEmpty()) {
            return "EMPTY No hotels found in " + city;
        }
        StringBuilder sb = new StringBuilder();
        for (Hotel h : hotels) {
            sb.append(h.getId()).append("|")
              .append(h.getName()).append("|")
              .append(h.getCity()).append("|")
              .append(h.getStars()).append("|")
              .append(h.getPrice()).append("|")
              .append(h.getRating()).append("|")
              .append(h.getImageUrl()).append("\n");
        }
        return sb.toString();
    }
    
    private String handleHotelDetail(int hotelId) {
        Hotel hotel = repository.getHotelById(hotelId);
        if (hotel == null) {
            return "ERROR Hotel not found";
        }
        return hotel.getId() + "|" +
               hotel.getName() + "|" +
               hotel.getCity() + "|" +
               hotel.getAddress() + "|" +
               hotel.getStars() + "|" +
               hotel.getPrice() + "|" +
               hotel.getRating() + "|" +
               hotel.getDescription() + "|" +
               hotel.getImageUrl();
    }
    
    private String handleAddHotel(String name, String city, String address, int stars, int price, String description, int ownerId) {
        int id = repository.addHotel(name, city, address, stars, price, description, ownerId);
        if (id > 0) {
            return "SUCCESS Hotel added with ID: " + id;
        }
        return "ERROR Failed to add hotel";
    }
    
    private String handleMyHotels(int ownerId) {
        List<Hotel> hotels = repository.getHotelsByOwner(ownerId);
        if (hotels.isEmpty()) {
            return "EMPTY You haven't added any hotels yet";
        }
        StringBuilder sb = new StringBuilder();
        for (Hotel h : hotels) {
            sb.append(h.getId()).append("|")
              .append(h.getName()).append("|")
              .append(h.getCity()).append("|")
              .append(h.getStars()).append("|")
              .append(h.getPrice()).append("|")
              .append(h.getStatus()).append("\n");
        }
        return sb.toString();
    }
    
    private String handleAddReview(int hotelId, int userId, int bookingId, int rating, String comment) {
        boolean success = repository.addReview(hotelId, userId, bookingId, rating, comment);
        if (success) {
            return "SUCCESS Review added";
        }
        return "ERROR Failed to add review";
    }
    
    private String handleGetReviews(int hotelId) {
        List<Review> reviews = repository.getReviewsByHotel(hotelId);
        if (reviews.isEmpty()) {
            return "EMPTY No reviews yet";
        }
        StringBuilder sb = new StringBuilder();
        for (Review r : reviews) {
            sb.append(r.getRating()).append("★|")
              .append(r.getComment()).append("|")
              .append(r.getCreatedAt()).append("\n");
        }
        return sb.toString();
    }
}