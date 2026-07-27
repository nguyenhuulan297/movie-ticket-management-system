package handler;

import exception.AuthenticationException;
import exception.EmailAlreadyExistsException;
import model.Customer;
import model.CustomerType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthHandler {

    private final AuthService authService;

    public AuthHandler(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Customer customer = authService.register(
                    request.getCustomerType(), request.getFullName(), request.getPhone(),
                    request.getEmail(), request.getPassword(),
                    request.getStudentId(), request.getSchoolName(),
                    request.getMemberSince(), request.getPointBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Customer customer = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(customer);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    static class RegisterRequest {
        private String fullName;
        private String phone;
        private String email;
        private String password;
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
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
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

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
