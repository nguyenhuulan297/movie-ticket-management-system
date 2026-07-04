package utils;

/**
 * Hàm kiểm tra dữ liệu đầu vào.
 */
public class Validator {

    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean validateEmail(String email) {
        return email != null && email.contains("@");
    }

    public static void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Giá vé phải lớn hơn 0");
        }
    }
}
