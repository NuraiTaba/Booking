package model;

public class Hotel {
    private int id;
    private String name;
    private String city;
    private String address;
    private int stars;
    private int price;
    private double rating;
    private String description;
    private String imageUrl;
    private int ownerId;
    private String status;
    
    public Hotel(int id, String name, String city, String address, int stars, int price, double rating, String description, String imageUrl, int ownerId, String status) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.stars = stars;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.status = status;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public int getStars() { return stars; }
    public int getPrice() { return price; }
    public double getRating() { return rating; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getOwnerId() { return ownerId; }
    public String getStatus() { return status; }
    
    @Override
    public String toString() {
        return id + "|" + name + "|" + city + "|" + stars + "|" + price + "|" + rating;
    }
}