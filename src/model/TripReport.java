package model;

import java.util.List;

public class TripReport {

    private String tripTitle;
    private int participants;
    private double profit;

    private List<OvernightStay> overnightStays;

    public TripReport(String tripTitle, int participants, double profit) {
        this.tripTitle = tripTitle;
        this.participants = participants;
        this.profit = profit;
    }

    public void setOvernightStays(List<OvernightStay> overnightStays) {
        this.overnightStays = overnightStays;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public int getParticipants() {
        return participants;
    }

    public double getProfit() {
        return profit;
    }

    public List<OvernightStay> getOvernightStays() {
        return overnightStays;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (OvernightStay overnightStay : overnightStays) {
            builder.append(overnightStay.toString());
            builder.append("\n");
        }

        return String.format("Guadagno da %s: %.2f€ (%d partecipanti)\nTappe:\n%s", tripTitle, profit, participants, builder);
    }
}