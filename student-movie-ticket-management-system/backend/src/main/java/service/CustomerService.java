package service;

import exception.CustomerNotFoundException;
import model.Customer;
import model.CustomerType;
import model.StudentCustomer;
import model.VIPCustomer;
import org.springframework.stereotype.Service;
import repository.CustomerRepository;
import utils.Validator;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(String id) {
        return customerRepository.findById(id);
    }

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
        if (customer == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        if (!Validator.validatePhone(customer.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (!Validator.validateEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        if (customer instanceof StudentCustomer student
                && (student.getStudentId() == null || student.getStudentId().isBlank())) {
            throw new IllegalArgumentException("Khách hàng sinh viên phải có mã sinh viên");
        }
        if (customer instanceof VIPCustomer vip
                && (vip.getMemberSince() == null || vip.getMemberSince().isBlank())) {
            throw new IllegalArgumentException("Khách hàng VIP phải có ngày trở thành thành viên");
        }
    }
}
