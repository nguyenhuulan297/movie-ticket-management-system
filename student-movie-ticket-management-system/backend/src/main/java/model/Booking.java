package model;

import java.util.List;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Showtime showtime;
    private List<Seat> seats;
    private String bookingTime;

    public Booking() {}

    public Booking(String bookingId, Customer customer, Showtime showtime, List<Seat> seats, String bookingTime) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.showtime = showtime;
        this.seats = seats;
        this.bookingTime = bookingTime;
    }

    // Getters & Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customer=" + (customer != null ? customer.getUserId() : null) +
                ", showtime=" + (showtime != null ? showtime.getShowtimeId() : null) +
                ", seatsCount=" + (seats != null ? seats.size() : 0) +
                ", bookingTime='" + bookingTime + '\'' +
                '}';
    }
}