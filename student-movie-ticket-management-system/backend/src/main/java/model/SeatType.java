package model;

public enum SeatType {
    NORMAL(0.0),
    DOUBLE(0.20),
    VIP(0.30);

    private final double surchargeRate;

    SeatType(double surchargeRate) {
        this.surchargeRate = surchargeRate;
    }

    public double getSurchargeRate() {
        return surchargeRate;
    }

    /**
     * Áp phụ phí ghế (Double +20%, VIP +30%) lên giá vé cơ bản.
     */
    public double applySurcharge(double basePrice) {
        return basePrice * (1 + surchargeRate);
    }
}
