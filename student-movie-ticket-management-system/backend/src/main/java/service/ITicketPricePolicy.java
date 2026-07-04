package service;

import model.SeatType;

public interface ITicketPricePolicy {
    double calculatePrice(double basePrice, SeatType seatType);

}
