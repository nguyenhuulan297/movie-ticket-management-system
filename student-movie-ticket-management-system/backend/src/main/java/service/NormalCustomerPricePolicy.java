package service;

import model.SeatType;

public class NormalCustomerPricePolicy implements ITicketPricePolicy {
    @Override
    public double calculatePrice(double basePrice, SeatType seatType) {
        double multiplier = switch (seatType) {
            case VIP -> 1.30;
            case DOUBLE -> 1.20;
            default -> 1.0;
        };
        return basePrice * multiplier;
    }
}
