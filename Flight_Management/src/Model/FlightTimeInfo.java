
package Model;

public class FlightTimeInfo {

    private String departureCity;
    private String destinationCity;
    private double flightTime;

    public FlightTimeInfo(String departureCity, String destinationCity, double flightTime) {
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.flightTime = flightTime;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public double getFlightTime() {
        return flightTime;
    }

    @Override
    public String toString() {
        return departureCity + "," + destinationCity + "," + flightTime;
    }
}
