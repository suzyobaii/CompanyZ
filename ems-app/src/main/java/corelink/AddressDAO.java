package corelink;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    //add a new address for an employee
    public boolean addAddress(int empid, String street, int cityId, int stateId, String zip,
                              String gender, String identifiedRace, Date dob, String mobilePhone) {
        String sql = "INSERT INTO address " +
                     "(empid, street, city_id, state_id, zip, gender, identified_race, dob, mobile_phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empid);
            ps.setString(2, street);
            ps.setInt(3, cityId);
            ps.setInt(4, stateId);
            ps.setString(5, zip);
            ps.setString(6, gender);
            ps.setString(7, identifiedRace);
            ps.setDate(8, dob);
            ps.setString(9, mobilePhone);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException ex) {
            System.out.println("Error in addAddress: " + ex.getMessage());
            return false;
        }
    }

    //update an employee's address/demographic info
    public boolean updateAddress(int empid, String street, int cityId, int stateId, String zip,
                                 String gender, String identifiedRace, Date dob, String mobilePhone) {
        String sql = "UPDATE address SET street = ?, city_id = ?, state_id = ?, zip = ?, " +
                     "gender = ?, identified_race = ?, dob = ?, mobile_phone = ? WHERE empid = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, street);
            ps.setInt(2, cityId);
            ps.setInt(3, stateId);
            ps.setString(4, zip);
            ps.setString(5, gender);
            ps.setString(6, identifiedRace);
            ps.setDate(7, dob);
            ps.setString(8, mobilePhone);
            ps.setInt(9, empid);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException ex) {
            System.out.println("Error in updateAddress: " + ex.getMessage());
            return false;
        }
    }

    //get a single employee's address info
    public Address getAddressByEmpId(int empid) {
        String sql = "SELECT a.empid, a.street, a.city_id, c.name AS city_name, " +
                     "a.state_id, s.name AS state_name, a.zip, a.gender, a.identified_race, a.dob, a.mobile_phone " +
                     "FROM address a " +
                     "JOIN city c ON a.city_id = c.city_id " +
                     "JOIN state s ON a.state_id = s.state_id " +
                     "WHERE a.empid = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Address addr = new Address();
                addr.setEmpid(rs.getInt("empid"));
                addr.setStreet(rs.getString("street"));
                addr.setCityId(rs.getInt("city_id"));
                addr.setCityName(rs.getString("city_name"));
                addr.setStateId(rs.getInt("state_id"));
                addr.setStateName(rs.getString("state_name"));
                addr.setZip(rs.getString("zip"));
                addr.setGender(rs.getString("gender"));
                addr.setIdentifiedRace(rs.getString("identified_race"));
                addr.setDob(rs.getDate("dob"));
                addr.setMobilePhone(rs.getString("mobile_phone"));
                return addr;
            }

        } catch (SQLException ex) {
            System.out.println("Error in getAddressByEmpId: " + ex.getMessage());
        }

        return null;
    }

    //get all addresses ~ for admin reporting
    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT a.empid, a.street, a.city_id, c.name AS city_name, " +
                     "a.state_id, s.name AS state_name, a.zip, a.gender, a.identified_race, a.dob, a.mobile_phone " +
                     "FROM address a " +
                     "JOIN city c ON a.city_id = c.city_id " +
                     "JOIN state s ON a.state_id = s.state_id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Address addr = new Address();
                addr.setEmpid(rs.getInt("empid"));
                addr.setStreet(rs.getString("street"));
                addr.setCityId(rs.getInt("city_id"));
                addr.setCityName(rs.getString("city_name"));
                addr.setStateId(rs.getInt("state_id"));
                addr.setStateName(rs.getString("state_name"));
                addr.setZip(rs.getString("zip"));
                addr.setGender(rs.getString("gender"));
                addr.setIdentifiedRace(rs.getString("identified_race"));
                addr.setDob(rs.getDate("dob"));
                addr.setMobilePhone(rs.getString("mobile_phone"));
                addresses.add(addr);
            }

        } catch (SQLException ex) {
            System.out.println("Error in getAllAddresses: " + ex.getMessage());
        }

        return addresses;
    }
}
