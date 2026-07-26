package service;

<<<<<<< Updated upstream
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import repository.CustomerRepository;
import utils.IdGenerator;
=======
import exception.CustomerNotFoundException;
import model.Customer;
import model.CustomerType;
import model.StudentCustomer;
import model.VIPCustomer;
import org.springframework.stereotype.Service;
import repository.CustomerRepository;
>>>>>>> Stashed changes
import utils.Validator;

import java.util.List;

<<<<<<< Updated upstream
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository();
        IdGenerator.initCustomerCounter(customerRepository.findAll().stream().map(Customer::getUserId).toList());
=======
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
>>>>>>> Stashed changes
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(String id) {
        return customerRepository.findById(id);
    }

<<<<<<< Updated upstream
    public Customer save(Customer customer) {
        validateCustomer(customer);
        if (customer.getUserId() == null || customer.getUserId().isBlank()) {
            customer.setUserId(IdGenerator.generateCustomerId());
        }
        customerRepository.save(customer);
        return customer;
    }

    public Customer update(Customer customer) {
        validateCustomer(customer);
        customerRepository.update(customer);
        return customer;
    }

    public void deleteById(String id) {
        customerRepository.deleteById(id);
    }

    public ITicketPricePolicy getPricePolicy(CustomerType type) {
        return switch (type) {
            case STUDENT -> new StudentPricePolicy();
            case VIP -> new VIPPricePolicy();
            default -> new NormalCustomerPricePolicy();
        };
    }

    private void validateCustomer(Customer customer) {
=======
    public Customer create(Customer customer) {
        validate(customer);
        return customerRepository.save(customer);
    }

    public Customer update(String id, Customer customer) {
        if (customerRepository.findById(id) == null) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng với ID: " + id);
        }
        validate(customer);
        customer.setUserId(id);
        return customerRepository.update(customer);
    }

    public void deleteById(String id) {
        if (!customerRepository.deleteById(id)) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng với ID: " + id);
        }
    }

    public ITicketPricePolicy getPricePolicy(CustomerType type) {
        switch (type) {
            case STUDENT: return new StudentPricePolicy();
            case VIP: return new VIPPricePolicy();
            default: return new NormalCustomerPricePolicy();
        }
    }

    private void validate(Customer customer) {
>>>>>>> Stashed changes
        if (customer == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
<<<<<<< Updated upstream
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
=======
            throw new IllegalArgumentException("Họ tên không được để trống");
>>>>>>> Stashed changes
        }
        if (!Validator.validatePhone(customer.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (!Validator.validateEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
<<<<<<< Updated upstream
        if (customer.getCustomerType() == null) {
            customer.setCustomerType(CustomerType.NORMAL);
        }
        if (customer.getCustomerType() == CustomerType.STUDENT) {
            if (!(customer instanceof StudentCustomer student)
                    || student.getStudentId() == null || student.getStudentId().isBlank()) {
                throw new IllegalArgumentException("Khách hàng sinh viên phải có studentId");
            }
        }
        if (customer.getCustomerType() == CustomerType.VIP) {
            if (!(customer instanceof VIPCustomer vip)
                    || vip.getMemberSince() == null || vip.getMemberSince().isBlank()) {
                throw new IllegalArgumentException("Khách hàng VIP phải có memberSince");
            }
=======
        if (customer instanceof StudentCustomer student
                && (student.getStudentId() == null || student.getStudentId().isBlank())) {
            throw new IllegalArgumentException("Khách hàng sinh viên phải có mã sinh viên");
        }
        if (customer instanceof VIPCustomer vip
                && (vip.getMemberSince() == null || vip.getMemberSince().isBlank())) {
            throw new IllegalArgumentException("Khách hàng VIP phải có ngày trở thành thành viên");
>>>>>>> Stashed changes
        }
    }
}
