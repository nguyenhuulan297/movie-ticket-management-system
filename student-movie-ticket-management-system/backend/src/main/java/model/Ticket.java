package model;

public class Ticket {
    private String ticketId;
    private Customer customer;
    private Showtime showtime;
    private Seat seat;
    private double finalPrice;
    private PaymentStatus paymentStatus;
    private String bookedAt;

    public Ticket() {}

    public Ticket(String ticketId, Customer customer, Showtime showtime, Seat seat,
                  double finalPrice, PaymentStatus paymentStatus, String bookedAt) {
        this.ticketId = ticketId;
        this.customer = customer;
        this.showtime = showtime;
        this.seat = seat;
        this.finalPrice = finalPrice;
        this.paymentStatus = paymentStatus;
        this.bookedAt = bookedAt;
    }

    // Getters & Setters
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getBookedAt() { return bookedAt; }
    public void setBookedAt(String bookedAt) { this.bookedAt = bookedAt; }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", customer=" + (customer != null ? customer.getUserId() : null) +
                ", showtime=" + (showtime != null ? showtime.getShowtimeId() : null) +
                ", seat=" + (seat != null ? seat.getSeatId() : null) +
                ", finalPrice=" + finalPrice +
                ", paymentStatus=" + paymentStatus +
                ", bookedAt='" + bookedAt + '\'' +
                '}';
    }
}