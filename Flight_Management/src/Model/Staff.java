
package Model;

public class Staff {

    private String staffId;
    private String staffName;
    private String staffRole;

    public Staff(String staffId, String staffName, String staffRole) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffRole = staffRole;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    @Override
    public String toString() {
        return staffId + "," + staffName + "," + staffRole;
    }

}
