
package Model;

public class InfoCustomer {

    private String identityId;
    private String nameCus;

    public InfoCustomer() {
    }

    public InfoCustomer(String identityId, String nameCus) {
        this.identityId = identityId;
        this.nameCus = nameCus;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getNameCus() {
        return nameCus;
    }

    @Override
    public String toString() {
        return identityId + "," + nameCus;
    }

}
