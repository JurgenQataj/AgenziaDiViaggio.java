package model;

public class Hotel {

    private int code;
    private String name;
    private String referee;
    private int capacity;
    private String street;
    private String civic;
    private String cp;
    private String email;
    private String telephone;
    private String fax;
    private Place place;

    public Hotel(String name) {
        this.name = name;
    }

    public Hotel(int code, String name, String referee, int capacity, String street, String civic, String cp, String email, String telephone, String fax) {
        this.code = code;
        this.name = name;
        this.referee = referee;
        this.capacity = capacity;
        this.street = street;
        this.civic = civic;
        this.cp = cp;
        this.email = email;
        this.telephone = telephone;
        this.fax = fax;
    }

    public Hotel(String name, String referee, int capacity, String street, String civic, String cp, String email, String telephone, String fax) {
        this.name = name;
        this.referee = referee;
        this.capacity = capacity;
        this.street = street;
        this.civic = civic;
        this.cp = cp;
        this.email = email;
        this.telephone = telephone;
        this.fax = fax;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getReferee() {
        return referee;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStreet() {
        return street;
    }

    public String getCivic() {
        return civic;
    }

    public String getCp() {
        return cp;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFax() {
        return fax;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return String.format("· (%d) %s, %s %s, %s, %s\n  Capienza: %d\n  Contatti:\n\t- Email: %s\n\t- Telefono: %s\n\t- Fax: %s\n",
                code, name, street, civic, cp, place.toString(), capacity, email, telephone, fax);
    }
}