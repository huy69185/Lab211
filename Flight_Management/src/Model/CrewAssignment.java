package Model;

import java.io.Serializable;

public class CrewAssignment implements Serializable {

    private String crewNumber;
    private String pilot;
    private String coPilot;
    private String flightAttendant;
    private String doctor;
    private String aviationTechnician;
    private String groundStaff;

    public CrewAssignment(String crewNumber, String pilot, String coPilot, String flightAttendant, String doctor, String aviationTechnician, String groundStaff) {
        this.crewNumber = crewNumber;
        this.pilot = pilot;
        this.coPilot = coPilot;
        this.flightAttendant = flightAttendant;
        this.doctor = doctor;
        this.aviationTechnician = aviationTechnician;
        this.groundStaff = groundStaff;
    }

    // Getter methods for CrewAssignment attributes
    public String getCrewNumber() {
        return crewNumber;
    }

    public String getPilot() {
        return pilot;
    }

    public String getCoPilot() {
        return coPilot;
    }

    public String getFlightAttendant() {
        return flightAttendant;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getAviationTechnician() {
        return aviationTechnician;
    }

    public String getGroundStaff() {
        return groundStaff;
    }

    @Override
    public String toString() {
        return crewNumber + "," + pilot + "," + coPilot + "," + flightAttendant + "," + doctor + "," + aviationTechnician + "," + groundStaff;
    }

}
