package model;

import java.util.List;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Showtime showtime;
    private List<Ticket> tickets;
    private double totalPrice;
    private String bookingTime;

    public Booking() {}

    public Booking(String bookingId, Customer customer, Showtime showtime,
                   List<Ticket> tickets, double totalPrice, String bookingTime) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.showtime = showtime;
        this.tickets = tickets;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
    }

    // Getters & Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }
    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customer=" + (customer != null ? customer.getUserId() : null) +
                ", showtime=" + (showtime != null ? showtime.getShowtimeId() : null) +
                ", ticketsCount=" + (tickets != null ? tickets.size() : 0) +
                ", totalPrice=" + totalPrice +
                ", bookingTime='" + bookingTime + '\'' +
                '}';
    }
}