package service;

import exception.AuthenticationException;
import exception.EmailAlreadyExistsException;
import model.Customer;
import model.CustomerType;
import org.springframework.stereotype.Service;
import repository.CustomerRepository;
import utils.PasswordUtils;
import utils.Validator;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public AuthService(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    /**
     * Đăng ký tài khoản khách hàng mới với email + mật khẩu.
     * Tái sử dụng validate() của CustomerService cho các field chung (tên, sđt, email, studentId, memberSince).
     */
    public Customer register(CustomerType type, String fullName, String phone, String email, String password,
                              String studentId, String schoolName, String memberSince, int pointBalance) {
        if (!Validator.validatePassword(password)) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (customerRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("Email đã được sử dụng: " + email);
        }

        Customer customer = CustomerFactory.create(type, fullName, phone, email,
                studentId, schoolName, memberSince, pointBalance);
        customer.setPassword(PasswordUtils.hash(password));

        Customer created = customerService.create(customer);
        created.setPassword(null);
        return created;
    }

    /**
     * Đăng nhập bằng email + mật khẩu. Trả về thông báo lỗi chung chung để tránh lộ
     * thông tin tài khoản nào tồn tại trong hệ thống.
     */
    public Customer login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null || !PasswordUtils.matches(password, customer.getPassword())) {
            throw new AuthenticationException("Email hoặc mật khẩu không đúng");
        }
        customer.setPassword(null);
        return customer;
    }
}
