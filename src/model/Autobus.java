package model;

public class Autobus {
    private final String targa;
    private final int capienza;
    private final double costoGiornaliero;

    public Autobus(String targa, int capienza, double costoGiornaliero) {
        this.targa = targa;
        this.capienza = capienza;
        this.costoGiornaliero = costoGiornaliero;
    }

    // Getters
    public String getTarga() {
        return targa;
    }
    public int getCapienza() {
        return capienza;
    }
    public double getCostoGiornaliero() {
        return costoGiornaliero;
    }

    @Override
    public String toString() {
        return String.format("Autobus [Targa: %s, Capienza: %d, Costo/Giorno: %.2f €]",
                targa, capienza, costoGiornaliero);
    }
}