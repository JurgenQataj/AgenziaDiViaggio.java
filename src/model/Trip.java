package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Trip {

    private String title;
    private LocalDate startDate;
    private LocalDate returnDate;
    private Double price;

    public Trip(String title, LocalDate startDate, LocalDate returnDate, Double price) {
        this.title = title;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return Date.valueOf(startDate);
    }

    public Date getReturnDate() {
        return Date.valueOf(returnDate);
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {

        return String.format("· \"%s\" (%s - %s): %.2f €\n",
                title,
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                price);
    }
}