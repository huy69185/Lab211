package Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hotelId;
    private String hotelName;
    private int roomAvailable;
    private String address;
    private String phone;
    private double rating;

    public Hotel(String hotelId, String hotelName, int roomAvailable, String address, String phone, double rating) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.roomAvailable = roomAvailable;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getRoomAvailable() {
        return roomAvailable;
    }

    public void setRoomAvailable(int roomAvailable) {
        this.roomAvailable = roomAvailable;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void display() {
        System.out.println("Hotel ID: " + hotelId);
        System.out.println("Hotel Name: " + hotelName);
        System.out.println("Rooms Available: " + roomAvailable);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Rating: " + rating);
        System.out.println("--------------------------");
    }
}
