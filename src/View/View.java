package View;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public abstract class View {

    abstract int showMenu() throws IOException;
    public void showMessage(String message) {
        System.out.println(message);
    }
    public <T> void printObjects(List<T> objects) {
        for (T t : objects) {
            System.out.println(t.toString());
        }
    }

    protected LocalDate parseStringDate(String stringDate) {
        return LocalDate.of(Integer.parseInt(stringDate.substring(6)),
                Integer.parseInt(stringDate.substring(3, 4)),
                Integer.parseInt(stringDate.substring(0, 1)));
    }
}