package Model;

import java.io.Serializable;

public class Reservation implements Serializable {

    private String reservationID;
    private String passengerName;
    private String identityCard;
    private String contactDetails;
    private String flightNumber;
    private boolean isVIP;
    private String seatNumber;

    public Reservation(String reservationID, String passengerName, String identityCard, String contactDetails, String flightNumber, boolean isVIP, String seatNumber) {
        this.reservationID = reservationID;
        this.passengerName = passengerName;
        this.identityCard = identityCard;
        this.contactDetails = contactDetails;
        this.flightNumber = flightNumber;
        this.isVIP = isVIP;
        this.seatNumber = seatNumber;
    }

    // Getter methods for Reservation attributes
    public String getReservationID() {
        return reservationID;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public boolean isIsVIP() {
        return isVIP;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    @Override
    public String toString() {
        return reservationID + "," + passengerName + "," + identityCard + "," + contactDetails + "," + flightNumber + "," + isVIP + "," + seatNumber;
    }

}
