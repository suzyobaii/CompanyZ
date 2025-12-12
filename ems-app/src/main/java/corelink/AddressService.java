package corelink;

import java.sql.Date;
import java.util.List;

public class AddressService {
    private final AddressDAO addressDAO = new AddressDAO();

    public boolean addAddress(int empid, String street, int cityId, int stateId, String zip, 
    String gender, String identifiedRace, Date dob, String mobilePhone) {
        return addressDAO.addAddress(empid, street, cityId, stateId, zip, gender, identifiedRace, dob, mobilePhone);
    }

    public boolean updateAddress(int empid, String street, int cityId, int stateId, String zip, 
    String gender, String identifiedRace, Date dob, String mobilePhone) {
        return addressDAO.updateAddress(empid, street, cityId, stateId, zip, gender, identifiedRace, dob, mobilePhone);
    }
    
    public Address getAddressByEmpId(int empid) {
        return addressDAO.getAddressByEmpId(empid);
    }

    public List<Address> getAllAddresses() {
        return addressDAO.getAllAddresses();
    }
}
