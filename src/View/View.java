package View;
import java.util.List;
public abstract class View {

    public void showMessage(String message) {
        System.out.println(message);
    }

    public <T> void printObjects(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            System.out.println("Nessun risultato da mostrare.");
            return;
        }
        for (T obj : objects) {
            System.out.println(obj.toString());
        }
    }
    public void printError(Exception e) {
        System.err.println("ERRORE: " + e.getMessage());
    }
}