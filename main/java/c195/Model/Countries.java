package c195.Model;

/**
 * Represents a country with a unique ID and name.
 * This class provides a structured way to manage country information, including getters and setters for accessing and modifying the country's ID and name.
 */
public class Countries {

    private int countryId;
    private String country;

    /**
     * Constructs a Countries instance with the specified country ID and name.
     * @param countryId The unique ID for the country.
     * @param country The name of the country.
     */
    public Countries(int countryId, String country){
        this.countryId = countryId;
        this.country = country;
    }


    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
