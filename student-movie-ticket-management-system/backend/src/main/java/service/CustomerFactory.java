package service;

import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;

/**
 * Tạo đúng subclass Customer (Normal/Student/VIP) từ dữ liệu thô.
 * Dùng chung bởi CustomerHandler (tạo khách hàng) và AuthService (đăng ký tài khoản).
 */
public class CustomerFactory {

    private CustomerFactory() {}

    public static Customer create(CustomerType type, String fullName, String phone, String email,
                                   String studentId, String schoolName,
                                   String memberSince, int pointBalance) {
        CustomerType safeType = type != null ? type : CustomerType.NORMAL;
        switch (safeType) {
            case STUDENT:
                return new StudentCustomer(null, fullName, phone, email, studentId, schoolName);
            case VIP:
                return new VIPCustomer(null, fullName, phone, email, memberSince, pointBalance);
            default:
                return new NormalCustomer(null, fullName, phone, email);
        }
    }
}
