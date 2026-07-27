package service;

import model.SeatType;

public class NormalCustomerPricePolicy implements ITicketPricePolicy {
    @Override
    public double calculatePrice(double basePrice, SeatType seatType) {
<<<<<<< Updated upstream
        double multiplier = switch (seatType) {
            case VIP -> 1.30;
            case DOUBLE -> 1.20;
            default -> 1.0;
        };
        return basePrice * multiplier;
=======
        return seatSurcharge(basePrice, seatType);
    }

    private double seatSurcharge(double basePrice, SeatType seatType) {
        switch (seatType) {
            case VIP: return basePrice * 1.30;
            case DOUBLE: return basePrice * 1.20;
            default: return basePrice;
        }
>>>>>>> Stashed changes
    }
}
