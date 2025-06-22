package model;

public class Place {

    private String name;
    private String country;

    public Place(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, country);
    }
}
