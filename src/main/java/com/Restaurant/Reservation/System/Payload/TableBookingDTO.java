package com.Restaurant.Reservation.System.Payload;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TableBookingDTO {
    private Long id;

    @Min(value = 1, message = "Table number must be at least 1")
    @Max(value = 20, message = "Table number can be at most 20")
    private int tableNumber;

    @NotNull(message = "Booking date and time must not be null")
    private LocalDateTime bookingDateTime;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 5, message = "Number of guests can be at most 5")
    private int numberOfGuests;

    public TableBookingDTO() {
    }

    public TableBookingDTO(int tableNumber, LocalDateTime bookingDateTime, int numberOfGuests) {
        this.tableNumber = tableNumber;
        this.bookingDateTime = bookingDateTime;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}


