package model;

public class Booking {

    private int code;
    private String tripTitle;
    private String user;
    private int participants;

    public Booking(String tripTitle, String user, int participants) {
        this.tripTitle = tripTitle;
        this.user = user;
        this.participants = participants;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getUser() {
        return user;
    }

    public int getParticipants() {
        return participants;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
