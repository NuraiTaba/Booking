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
        String[] parts = request.contains("|") ? request.split("\\|", -1) : request.split(" ");
        if (parts.length == 0) {
            return "ERROR Empty command";
        }
        
        String command = parts[0];
        System.out.println("📥 Received command: " + command);
        
        switch (command) {
            case "OWNER_BOOKINGS":
    if (parts.length == 2) {
        try {
            int ownerId = Integer.parseInt(parts[1]);
            return repository.getOwnerBookings(ownerId);
        } catch (NumberFormatException e) {
            return "ERROR Invalid ownerId";
        }
    }
    return "ERROR Usage: OWNER_BOOKINGS ownerId";
            case "UPDATE_HOTEL":
                if (parts.length == 9) {
                try {
             int hotelId = Integer.parseInt(parts[1]);
             String name = parts[2];
             String city = parts[3];
             String address = parts[4];
             int stars = Integer.parseInt(parts[5]);
             int price = Integer.parseInt(parts[6]);
             String description = parts[7];
                int ownerId = Integer.parseInt(parts[8]);
            return handleUpdateHotel(hotelId, name, city, address, stars, price, description, ownerId);
                } catch (NumberFormatException e) {
            return "ERROR Invalid number format";
                }
            }
            return "ERROR Usage: UPDATE_HOTEL id name city address stars price description ownerId";
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

            case "BOOK_FULL":
                if (parts.length >= 10) {
                    try {
                        int userId = Integer.parseInt(parts[1]);
                        int guests = Integer.parseInt(parts[5]);
                        int rooms = Integer.parseInt(parts[6]);
                        boolean success = repository.saveFullBooking(userId, parts[2], parts[3], parts[4],
                                guests, rooms, parts[7], parts[8], parts[9]);
                        return success ? "SUCCESS Booking confirmed" : "ERROR Failed to create booking";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid booking numbers";
                    }
                }
                return "ERROR Usage: BOOK_FULL userId hotel checkIn checkOut guests rooms guestName email phone";
                
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

            case "CANCEL_BOOKING":
                if (parts.length == 3) {
                    try {
                        boolean success = repository.cancelBooking(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        return success ? "SUCCESS Booking cancelled" : "ERROR Booking not found";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid booking id";
                    }
                }
                return "ERROR Usage: CANCEL_BOOKING userId bookingId";

            case "CHANGE_BOOKING_DATES":
                if (parts.length == 5) {
                    try {
                        boolean success = repository.changeBookingDates(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], parts[4]);
                        return success ? "SUCCESS Booking dates changed" : "ERROR Booking not found";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid booking id";
                    }
                }
                return "ERROR Usage: CHANGE_BOOKING_DATES userId bookingId checkIn checkOut";

            case "ADD_WISHLIST":
                if (parts.length >= 3) {
                    try {
                        String collection = parts.length >= 4 ? parts[3] : "Favorites";
                        boolean success = repository.addToWishlist(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), collection);
                        return success ? "SUCCESS Added to wishlist" : "ERROR Failed to add wishlist item";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid wishlist data";
                    }
                }
                return "ERROR Usage: ADD_WISHLIST userId hotelId collection";

            case "REMOVE_WISHLIST":
                if (parts.length == 3) {
                    try {
                        boolean success = repository.removeFromWishlist(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        return success ? "SUCCESS Removed from wishlist" : "ERROR Wishlist item not found";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid wishlist data";
                    }
                }
                return "ERROR Usage: REMOVE_WISHLIST userId hotelId";

            case "GET_WISHLIST":
                if (parts.length == 2) {
                    try {
                        return repository.getWishlist(Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: GET_WISHLIST userId";

            case "GET_PROFILE":
                if (parts.length == 2) {
                    try {
                        return repository.getUserProfile(Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: GET_PROFILE userId";

            case "UPDATE_PROFILE":
                if (parts.length == 5) {
                    try {
                        boolean success = repository.updateUserProfile(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4]);
                        return success ? "SUCCESS Profile updated" : "ERROR Profile not found";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: UPDATE_PROFILE userId fullName email phone";
                
            case "GET_HOTELS":
                return parts.length >= 2 ? handleGetHotels(parts[1]) : handleGetHotels("rating");
                
            case "SEARCH":
                if (parts.length >= 2) {
                    String city = parts[1];
                    return handleSearchHotels(city);
                }
                return "ERROR Usage: SEARCH city";

            case "SAVE_SEARCH":
                if (parts.length >= 3) {
                    try {
                        repository.saveRecentSearch(Integer.parseInt(parts[1]), parts[2]);
                        return "SUCCESS Search saved";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: SAVE_SEARCH userId query";

            case "RECENT_SEARCHES":
                if (parts.length == 2) {
                    try {
                        return repository.getRecentSearches(Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: RECENT_SEARCHES userId";

            case "POPULAR_DESTINATIONS":
                return repository.getPopularDestinations();

            case "AUTOCOMPLETE":
                if (parts.length >= 2) {
                    return repository.getAutocompleteSuggestions(parts[1]);
                }
                return repository.getAutocompleteSuggestions("");
                
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
                if (parts.length >= 8) {
                    try {
                        String name = parts[1];
                        String city = parts[2];
                        String address = parts[3];
                        int stars = Integer.parseInt(parts[4]);
                        int price = Integer.parseInt(parts[5]);
                        String description = parts[6];
                        String imageUrl = "";
                        int ownerId;
                        if (parts.length >= 9) {
                            imageUrl = parts[7];
                            ownerId = Integer.parseInt(parts[8]);
                        } else {
                            ownerId = Integer.parseInt(parts[7]);
                        }
                        String result = handleAddHotel(name, city, address, stars, price, description, imageUrl, ownerId);
                        if (result.startsWith("SUCCESS") && parts.length >= 18) {
                            int hotelId = Integer.parseInt(result.replaceAll("\\D+", ""));
                            repository.updateHotelExtrasById(hotelId, parts[9], parts[10],
                                    Double.parseDouble(parts[11]),
                                    0,
                                    Integer.parseInt(parts[12]),
                                    Boolean.parseBoolean(parts[13]),
                                    Boolean.parseBoolean(parts[14]),
                                    Boolean.parseBoolean(parts[15]),
                                    Boolean.parseBoolean(parts[16]),
                                    Boolean.parseBoolean(parts[17]));
                        }
                        return result;
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid number format";
                    }
                }
                return "ERROR Usage: ADD_HOTEL name city address stars price description imageUrl ownerId";
                
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
                
            case "ADD_REVIEW":
                if (parts.length >= 10) {
                    try {
                        boolean success = repository.addDetailedReview(
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5]),
                                Integer.parseInt(parts[6]),
                                Integer.parseInt(parts[7]),
                                parts[8],
                                parts[9]);
                        return success ? "SUCCESS Review added" : "ERROR Failed to add review";
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid review numbers";
                    }
                }
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
                if (parts.length >= 4) {
                    try {
                        return repository.getReviewsByHotelSorted(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid review filter";
                    }
                }
                if (parts.length == 2) {
                    try {
                        int hotelId = Integer.parseInt(parts[1]);
                        return handleGetReviews(hotelId);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid hotelId";
                    }
                }
                return "ERROR Usage: GET_REVIEWS hotelId";

            case "RECOMMENDATIONS":
                if (parts.length == 2) {
                    try {
                        return repository.getRecommendations(Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid userId";
                    }
                }
                return "ERROR Usage: RECOMMENDATIONS userId";

            case "CONTACT_HOTEL":
                if (parts.length >= 4) {
                    try {
                        return repository.contactHotel(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid contact data";
                    }
                }
                return "ERROR Usage: CONTACT_HOTEL userId hotelId message";
                
            case "FILTER":
                if (parts.length == 4) {
                    try {
                        int minPrice = Integer.parseInt(parts[1]);
                        int maxPrice = Integer.parseInt(parts[2]);
                        int stars = Integer.parseInt(parts[3]);
                        return handleFilter(minPrice, maxPrice, stars);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid price or stars";
                    }
                }
                if (parts.length >= 13) {
                    try {
                        return handleAdvancedFilter(
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                parts[4],
                                Boolean.parseBoolean(parts[5]),
                                Boolean.parseBoolean(parts[6]),
                                Boolean.parseBoolean(parts[7]),
                                Boolean.parseBoolean(parts[8]),
                                Boolean.parseBoolean(parts[9]),
                                parts[10],
                                Boolean.parseBoolean(parts[11]),
                                parts[12]);
                    } catch (NumberFormatException e) {
                        return "ERROR Invalid filter values";
                    }
                }
                return "ERROR Usage: FILTER minPrice maxPrice stars propertyType wifi parking pool breakfast freeCancellation district availableOnly sortBy";
                
            default:
                return "ERROR Unknown command: " + command;
        }
    }
    

    private String handleUpdateHotel(int hotelId, String name, String city, String address, int stars, int price, String description, int ownerId) {
    // Проверяем, что отель принадлежит этому владельцу
    List<Hotel> ownerHotels = repository.getHotelsByOwner(ownerId);
    boolean ownsHotel = ownerHotels.stream().anyMatch(h -> h.getId() == hotelId);
    if (!ownsHotel) {
        return "ERROR You don't own this hotel";
    }
    
    boolean success = repository.updateHotel(hotelId, name, city, address, stars, price, description);
    if (success) {
        return "SUCCESS Hotel updated";
    }
    return "ERROR Failed to update hotel";
}
    // ========== ОБРАБОТЧИКИ ==========
    
    private String handleFilter(int minPrice, int maxPrice, int stars) {
        List<Hotel> hotels = repository.filterHotels(minPrice, maxPrice, stars);
        return serializeHotels(hotels);
    }

    private String handleAdvancedFilter(int minPrice, int maxPrice, int stars, String propertyType,
                                        boolean wifi, boolean parking, boolean pool, boolean breakfast,
                                        boolean freeCancellation, String district, boolean availableOnly,
                                        String sortBy) {
        List<Hotel> hotels = repository.filterHotels(minPrice, maxPrice, stars, propertyType, wifi, parking,
                pool, breakfast, freeCancellation, district, availableOnly, sortBy);
        return serializeHotels(hotels);
    }

    private String serializeHotels(List<Hotel> hotels) {
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
    
    private String handleGetHotels(String sortBy) {
        System.out.println("🔍 handleGetHotels() called");
        List<Hotel> hotels = repository.getHotelsSorted(sortBy);
        System.out.println("Hotels found: " + hotels.size());
        return serializeHotels(hotels);
    }
    
    private String handleSearchHotels(String city) {
        List<Hotel> hotels = repository.searchHotels(city);
        if (hotels.isEmpty()) {
            return "EMPTY No hotels found in " + city;
        }
        return serializeHotels(hotels);
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
               hotel.getImageUrl() + "|" +
               repository.getHotelExtras(hotelId) + "|" +
               repository.getHotelContent(hotelId);
    }
    
    private String handleAddHotel(String name, String city, String address, int stars, int price, String description, String imageUrl, int ownerId) {
        int id = repository.addHotel(name, city, address, stars, price, description, imageUrl, ownerId);
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
