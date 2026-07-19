package com.example.cinema.model;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private Ticket ticket;
    private String method;
    private double amount;
    private LocalDateTime paidAt;
    private boolean success;

    // Constructor
    public Payment() {}

    public Payment(String paymentId, Ticket ticket, String method, double amount) {
        this.paymentId = paymentId;
        this.ticket = ticket;
        this.method = method;
        this.amount = amount;
        this.paidAt = LocalDateTime.now();
        this.success = false;
    }

    // Getters & Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}