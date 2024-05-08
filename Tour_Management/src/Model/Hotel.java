package Model;

import java.io.Serializable;

public class Hotel implements Serializable {
    public int id;
    public String name;
    public String location;
    public int roomsAvailable;
    public String[] amenities;
    public double pricePerNight;

    public Hotel(int id, String name, String location, int roomsAvailable, String[] amenities, double pricePerNight) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.roomsAvailable = roomsAvailable;
        this.amenities = amenities;
        this.pricePerNight = pricePerNight;
    }
}