package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeatTypeTest {

    private static final double DELTA = 0.0001;

    @Test
    void normalSeat_hasNoSurcharge() {
        assertEquals(100000.0, SeatType.NORMAL.applySurcharge(100000.0), DELTA);
    }

    @Test
    void doubleSeat_hasTwentyPercentSurcharge() {
        assertEquals(120000.0, SeatType.DOUBLE.applySurcharge(100000.0), DELTA);
    }

    @Test
    void vipSeat_hasThirtyPercentSurcharge() {
        assertEquals(130000.0, SeatType.VIP.applySurcharge(100000.0), DELTA);
    }

    @Test
    void vipAndDoubleSurcharge_areHigherThanNormal() {
        double basePrice = 90000.0;
        double normalPrice = SeatType.NORMAL.applySurcharge(basePrice);
        double doublePrice = SeatType.DOUBLE.applySurcharge(basePrice);
        double vipPrice = SeatType.VIP.applySurcharge(basePrice);

        assertEquals(true, doublePrice > normalPrice);
        assertEquals(true, vipPrice > normalPrice);
        assertEquals(true, vipPrice > doublePrice);
    }
}
