package utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sinh mã tự động cho các đối tượng.
 */
public class IdGenerator {
    private static AtomicInteger customerCounter = new AtomicInteger(1);

    public static String generateCustomerId() {
        return String.format("CUS%03d", customerCounter.getAndIncrement());
    }

    public static String generateTicketId() {
        return "TKT" + System.currentTimeMillis();
    }

    public static String generateBookingId() {
        return "BKG" + System.currentTimeMillis();
    }

    public static String generatePaymentId() {
        return "PAY" + System.currentTimeMillis();
    }
}
