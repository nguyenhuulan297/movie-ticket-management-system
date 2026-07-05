package utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sinh mã tự động cho các đối tượng.
 */
public class IdGenerator {
    private static final AtomicInteger customerCounter = new AtomicInteger(1);

    /**
     * Gọi 1 lần khi CustomerRepository khởi tạo, truyền vào danh sách customerId
     * đã có trong customers.json để tránh sinh trùng ID sau khi restart server.
     */
    public static void initCustomerCounter(List<String> existingIds) {
        int max = 0;
        if (existingIds != null) {
            for (String id : existingIds) {
                try {
                    int num = Integer.parseInt(id.replaceAll("[^0-9]", ""));
                    max = Math.max(max, num);
                } catch (NumberFormatException ignored) { }
            }
        }
        customerCounter.set(max + 1);
    }

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