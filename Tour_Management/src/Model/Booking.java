package Model;


import Model.Hotel;
import Model.Tour;
import java.io.Serializable;

public class Booking implements Serializable{

    public int bookingId;
    public Tour bookedTour;
    public Hotel bookedHotel;
    public int numberOfGuests;

    public Booking(int bookingId, Tour bookedTour, Hotel bookedHotel, int numberOfGuests) {
        this.bookingId = bookingId;
        this.bookedTour = bookedTour;
        this.bookedHotel = bookedHotel;
        this.numberOfGuests = numberOfGuests;
    }
}