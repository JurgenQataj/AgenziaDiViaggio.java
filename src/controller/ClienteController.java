package controller;

import View.ClienteView;
import model.Role;
import model.dao.ConnectionFactory;

import java.io.IOException;
import java.sql.SQLException;

public class ClienteController {

    private final ClienteView view;
    private final AppController appController;
    private final String clientEmail; // Memorizza l'email del cliente loggato

    /**
     * Costruttore corretto. Richiede solo l'AppController e l'email del cliente.
     * @param appController Il controller principale dell'applicazione.
     * @param clientEmail L'email dell'utente che ha effettuato il login.
     */
    public ClienteController(AppController appController, String clientEmail) {
        this.appController = appController;
        this.clientEmail = clientEmail;
        this.view = new ClienteView(); // Ogni controller ha la sua view
    }

    public void start() {
        ConnectionFactory.changeRole(Role.CLIENTE);
        boolean running = true;
        while (running) {
            try {
                int choice = view.showMenu(); // La view mostrerà il menu del cliente
                switch (choice) {
                    // Esempio di opzioni per il cliente
                    case 1:
                        // Logica per visualizzare i viaggi disponibili
                        // Esempio: new ListTripsDAO(...).execute();
                        break;
                    case 2:
                        // Logica per prenotare un viaggio
                        // Esempio: new BookDAO(...).execute();
                        break;
                    case 3:
                        // Logica per vedere le proprie prenotazioni
                        view.showMessage("Le tue prenotazioni (per " + this.clientEmail + "):");
                        // Qui chiamerai un DAO per ottenere le prenotazioni per questa email
                        // Esempio: new ListMyBookingsDAO(this.clientEmail).execute();
                        break;
                    case 4:
                        // Logica per cancellare una prenotazione
                        // Esempio: new CancelBookingDAO(...).execute();
                        break;
                    case 5:
                        appController.logout();
                        running = false;
                        break;
                    default:
                        view.showMessage("Opzione non valida.");
                }
                // ################### ECCO LA CORREZIONE ###################
                // Catturando la classe base 'Exception', gestiamo sia IOException
                // sia SQLException (quando verrà aggiunta la logica nel 'case')
                // e qualsiasi altro errore imprevisto, senza errori di compilazione.
            } catch (Exception e) {
                // #########################################################
                view.printError(e);
            }
        }
    }
}