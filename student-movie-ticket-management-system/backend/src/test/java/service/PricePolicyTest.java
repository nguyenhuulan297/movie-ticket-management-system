package service;

import model.SeatType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricePolicyTest {

    private static final double BASE_PRICE = 100_000;
    private static final double DELTA = 0.001;

    @Test
    void normalCustomerPolicy_appliesOnlySeatSurcharge() {
        ITicketPricePolicy policy = new NormalCustomerPricePolicy();

        assertEquals(100_000, policy.calculatePrice(BASE_PRICE, SeatType.NORMAL), DELTA);
        assertEquals(120_000, policy.calculatePrice(BASE_PRICE, SeatType.DOUBLE), DELTA);
        assertEquals(130_000, policy.calculatePrice(BASE_PRICE, SeatType.VIP), DELTA);
    }

    @Test
    void studentPolicy_appliesSeatSurchargeThen20PercentDiscount() {
        ITicketPricePolicy policy = new StudentPricePolicy();

        assertEquals(80_000, policy.calculatePrice(BASE_PRICE, SeatType.NORMAL), DELTA);
        assertEquals(96_000, policy.calculatePrice(BASE_PRICE, SeatType.DOUBLE), DELTA);
        assertEquals(104_000, policy.calculatePrice(BASE_PRICE, SeatType.VIP), DELTA);
    }

    @Test
    void vipPolicy_appliesSeatSurchargeThen30PercentDiscount() {
        ITicketPricePolicy policy = new VIPPricePolicy();

        assertEquals(70_000, policy.calculatePrice(BASE_PRICE, SeatType.NORMAL), DELTA);
        assertEquals(84_000, policy.calculatePrice(BASE_PRICE, SeatType.DOUBLE), DELTA);
        assertEquals(91_000, policy.calculatePrice(BASE_PRICE, SeatType.VIP), DELTA);
    }
}
