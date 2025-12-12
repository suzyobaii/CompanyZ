package corelink;

public class Address {

    private int empid;
    private String street;
    private int cityId;
    private String cityName;      
    private int stateId;
    private String stateName;     
    private String zip;
    private String gender;
    private String identifiedRace;
    private String mobilePhone;

    // Full constructor
    public Address(int empid, String street, int cityId, int stateId, String zip,
                   String gender, String identifiedRace, String mobilePhone) {
        this.empid = empid;
        this.street = street;
        this.cityId = cityId;
        this.stateId = stateId;
        this.zip = zip;
        this.gender = gender;
        this.identifiedRace = identifiedRace;
        this.mobilePhone = mobilePhone;
    }

    //empty constructor for DAO retrieval
    public Address() {}

    // ----- getters & setters -----
    public int getEmpid() { return empid; }
    public void setEmpid(int empid) { this.empid = empid; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getIdentifiedRace() { return identifiedRace; }
    public void setIdentifiedRace(String identifiedRace) { this.identifiedRace = identifiedRace; }

    public String getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(String mobilePhone) { this.mobilePhone = mobilePhone; }

    // ----- toString for printing addresses -----
    @Override
    public String toString() {
        return "Address Details\n=============================================\n" +
                "\nEmployee ID: " + empid +
                "\nStreet: " + street +
                "\nCity ID: " + cityId +
                "\nState ID: " + stateId +
                "\nZip: " + zip +
                "\nGender: " + gender +
                "\nRace: " + identifiedRace +
                "\nMobile Phone: " + mobilePhone;
    }
}
