package Model;

import java.io.Serializable;

public class Flight implements Serializable {

    private String flightNumber;
    private String departureCity;
    private String destinationCity;
    private String dateFlight;
    private String departureTime;
    private String arrivalTime;
    private int availableVIPSeats;
    private int availableNormalSeats;
    private CrewAssignment crewAssignment;

    public Flight() {
    }
    

    public Flight(String flightNumber, String departureCity, String destinationCity, String dateFlight, String departureTime, String arrivalTime, int availableVIPSeats, int availableNormalSeats) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.dateFlight = dateFlight;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableVIPSeats = availableVIPSeats;
        this.availableNormalSeats = availableNormalSeats;
    }

    // Getter and setter methods for Flight attributes
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDateFlight() {
        return dateFlight;
    }

    public void setDateFlight(String dateFlight) {
        this.dateFlight = dateFlight;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getAvailableVIPSeats() {
        return availableVIPSeats;
    }

    public void setAvailableVIPSeats(int availableVIPSeats) {
        this.availableVIPSeats = availableVIPSeats;
    }

    public int getAvailableNormalSeats() {
        return availableNormalSeats;
    }

    public void setAvailableNormalSeats(int availableNormalSeats) {
        this.availableNormalSeats = availableNormalSeats;
    }

    public CrewAssignment getCrewAssignment() {
        return crewAssignment;
    }

    public void setCrewAssignment(CrewAssignment crewAssignment) {
        this.crewAssignment = crewAssignment;
    }


    @Override
    public String toString() {
        return flightNumber + "," + departureCity + "," + destinationCity + "," + dateFlight + "," + departureTime + "," + arrivalTime + "," + availableVIPSeats + "," + availableNormalSeats + "," + crewAssignment;
    }
}
