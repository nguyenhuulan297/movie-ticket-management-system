package service;

import model.SeatType;

public interface TicketPricePolicy {
    double calculatePrice(double basePrice, SeatType seatType);
}
