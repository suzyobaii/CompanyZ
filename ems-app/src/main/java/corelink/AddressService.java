package corelink;

import java.util.List;

public class AddressService {
    private final AddressDAO addressDAO = new AddressDAO();

    public boolean addAddress(int empid, String street, int cityId, int stateId, String zip, 
    String gender, String identifiedRace, String mobilePhone) {
        return addressDAO.addAddress(empid, street, cityId, stateId, zip, gender, identifiedRace, mobilePhone);
    }

    public boolean updateAddress(int empid, String street, int cityId, int stateId, String zip, 
    String gender, String identifiedRace, String mobilePhone) {
        return addressDAO.updateAddress(empid, street, cityId, stateId, zip, gender, identifiedRace, mobilePhone);
    }
    
    public Address getAddressByEmpId(int empid) {
        return addressDAO.getAddressByEmpId(empid);
    }

    public List<Address> getAllAddresses() {
        return addressDAO.getAllAddresses();
    }
}
