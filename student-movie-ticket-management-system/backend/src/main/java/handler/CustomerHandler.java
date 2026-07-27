package handler;

import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerHandler {

    private final CustomerService customerService;

    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy khách hàng với ID: " + id);
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        try {
            Customer customer = toCustomer(request);
            Customer created = customerService.create(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Customer toCustomer(CustomerRequest request) {
        CustomerType type = request.getCustomerType() != null ? request.getCustomerType() : CustomerType.NORMAL;
        switch (type) {
            case STUDENT:
                return new StudentCustomer(null, request.getFullName(), request.getPhone(), request.getEmail(),
                        request.getStudentId(), request.getSchoolName());
            case VIP:
                return new VIPCustomer(null, request.getFullName(), request.getPhone(), request.getEmail(),
                        request.getMemberSince(), request.getPointBalance());
            default:
                return new NormalCustomer(null, request.getFullName(), request.getPhone(), request.getEmail());
        }
    }

    // DTO cho request body tạo khách hàng
    static class CustomerRequest {
        private String fullName;
        private String phone;
        private String email;
        private CustomerType customerType;
        private String studentId;
        private String schoolName;
        private String memberSince;
        private int pointBalance;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public CustomerType getCustomerType() { return customerType; }
        public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getSchoolName() { return schoolName; }
        public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
        public String getMemberSince() { return memberSince; }
        public void setMemberSince(String memberSince) { this.memberSince = memberSince; }
        public int getPointBalance() { return pointBalance; }
        public void setPointBalance(int pointBalance) { this.pointBalance = pointBalance; }
    }
}
