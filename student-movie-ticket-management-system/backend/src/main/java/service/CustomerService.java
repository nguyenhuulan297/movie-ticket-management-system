package service;

import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import repository.CustomerRepository;
import utils.IdGenerator;
import utils.Validator;

import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository();
        IdGenerator.initCustomerCounter(customerRepository.findAll().stream().map(Customer::getUserId).toList());
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(String id) {
        return customerRepository.findById(id);
    }

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
        if (customer == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        if (!Validator.validatePhone(customer.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (!Validator.validateEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
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
        }
    }
}
