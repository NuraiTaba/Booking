package model;

import java.io.Serializable;

public class Booking implements Serializable {
    private int userId;
    private String hotel;
    private String date;

    public Booking(int userId, String hotel, String date) {
        this.userId = userId;
        this.hotel = hotel;
        this.date = date;
    }

    public int getUserId() { return userId; }
    public String getHotel() { return hotel; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return "Booking [userId=" + userId + ", hotel=" + hotel + ", date=" + date + "]";
    }
}