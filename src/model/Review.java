package model;

public class Review {
    private int id;
    private int hotelId;
    private int userId;
    private int bookingId;
    private int rating;
    private String comment;
    private String createdAt;
    
    public Review(int id, int hotelId, int userId, int bookingId, int rating, String comment, String createdAt) {
        this.id = id;
        this.hotelId = hotelId;
        this.userId = userId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    
    public int getId() { return id; }
    public int getHotelId() { return hotelId; }
    public int getUserId() { return userId; }
    public int getBookingId() { return bookingId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return rating + "★|" + comment + "|" + createdAt;
    }
}