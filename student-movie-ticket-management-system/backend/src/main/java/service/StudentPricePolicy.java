package service;

import model.SeatType;

public class StudentPricePolicy implements ITicketPricePolicy {
    @Override
    public double calculatePrice(double basePrice, SeatType seatType) {
        return seatSurcharge(basePrice, seatType) * 0.80;
    }

    private double seatSurcharge(double basePrice, SeatType seatType) {
        switch (seatType) {
            case VIP: return basePrice * 1.30;
            case DOUBLE: return basePrice * 1.20;
            default: return basePrice;
        }
    }
}
