package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingDetails {
    private final int codice;
    private final int numeroPersone;
    private final String titoloViaggio;
    private final Date dataPartenza;

    public BookingDetails(int codice, int numeroPersone, String titoloViaggio, Date dataPartenza) {
        this.codice = codice;
        this.numeroPersone = numeroPersone;
        this.titoloViaggio = titoloViaggio;
        this.dataPartenza = dataPartenza;
    }

    public int getCodice() { return codice; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Prenotazione [Codice: " + codice + ", Viaggio: '" + titoloViaggio +
                "', Partenza: " + sdf.format(dataPartenza) + ", Persone: " + numeroPersone + "]";
    }
}