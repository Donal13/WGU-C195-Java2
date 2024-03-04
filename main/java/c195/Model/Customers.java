package c195.Model;

/**
 * Represents a customer with customer ID, name, address, postal code, phone number, and division ID.
 * This class is designed to encapsulate customer information, providing a structured way to store and access customer data.
 * Getters and setters are provided for each field to allow controlled access and modification.
 */
public class Customers {

    private int custId;
    private String custName;
    private String address;
    private String zipcode;
    private String phoneNum;
    private int divId;

    /**
     * Constructs a Customers instance with the specified details.
     * @param custId The unique ID of the customer.
     * @param custName The name of the customer.
     * @param address The address of the customer.
     * @param zipcode The postal code of the customer.
     * @param phoneNum The phone number of the customer.
     * @param divId The division ID of the customer.
     */
    public Customers(int custId, String custName, String address, String zipcode, String phoneNum, int divId) {
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.zipcode = zipcode;
        this.phoneNum = phoneNum;
        this.divId = divId;
    }


    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getDivId() {
        return divId;
    }

    public void setDivId(int divId) {
        this.divId = divId;
    }
}
