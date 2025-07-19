package model;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TripReport {
    private final String titoloViaggio;
    private final int partecipanti;
    private final double guadagnoOPerdita;
    private final List<OvernightStay> overnightStays; // Aggiunto campo per i pernottamenti

    // Costruttore aggiornato per accettare la lista dei pernottamenti
    public TripReport(String titoloViaggio, int partecipanti, double guadagnoOPerdita, List<OvernightStay> stays) {
        this.titoloViaggio = titoloViaggio;
        this.partecipanti = partecipanti;
        this.guadagnoOPerdita = guadagnoOPerdita;
        this.overnightStays = stays; // Inizializziamo il campo, risolvendo l'errore!
    }

    @Override
    public String toString() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.ITALY);
        StringBuilder sb = new StringBuilder();

        sb.append("Titolo Viaggio: ").append(titoloViaggio).append("\n");
        sb.append("Numero Partecipanti: ").append(partecipanti).append("\n");

        // Ora possiamo ciclare su 'overnightStays' perché non è più null
        if (overnightStays != null && !overnightStays.isEmpty()) {
            sb.append("Tappe del Viaggio:\n");
            for (OvernightStay stay : overnightStays) {
                sb.append("  - ").append(stay.toString()).append("\n");
            }
        }

        if (guadagnoOPerdita >= 0) {
            sb.append("Guadagno Finale: ").append(currencyFormatter.format(guadagnoOPerdita));
        } else {
            sb.append("Perdita Finale: ").append(currencyFormatter.format(guadagnoOPerdita));
        }

        return sb.toString();
    }
}