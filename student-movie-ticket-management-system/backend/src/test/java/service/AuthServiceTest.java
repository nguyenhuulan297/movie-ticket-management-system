package service;

import exception.AuthenticationException;
import exception.EmailAlreadyExistsException;
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CustomerRepository;
import utils.PasswordUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

    @Test
    void register_newEmail_createsCustomerWithHashedPasswordAndHidesItInResponse() {
        AuthService authService = new AuthService(customerRepository, customerService);
        when(customerRepository.findByEmail("new@email.com")).thenReturn(null);
        // Trả về 1 bản sao (không phải cùng reference) để việc AuthService null hóa mật khẩu
        // trên kết quả trả về không làm mất dấu vết mật khẩu đã băm được truyền vào create().
        when(customerService.create(any())).thenAnswer(inv -> {
            Customer arg = inv.getArgument(0);
            Customer copy = new NormalCustomer(arg.getUserId(), arg.getFullName(), arg.getPhone(), arg.getEmail());
            copy.setPassword(arg.getPassword());
            return copy;
        });

        Customer result = authService.register(CustomerType.NORMAL, "Nguyen Van A", "0901234567",
                "new@email.com", "secret123", null, null, null, 0);

        assertNull(result.getPassword());
        verify(customerService).create(argThat(c -> c.getPassword() != null && c.getPassword().contains(":")));
    }

    // ---- Tình huống: đăng ký trùng email ----
    @Test
    void register_existingEmail_throwsEmailAlreadyExistsException() {
        AuthService authService = new AuthService(customerRepository, customerService);
        when(customerRepository.findByEmail("taken@email.com"))
                .thenReturn(new NormalCustomer("CUS001", "Someone", "0900000000", "taken@email.com"));

        assertThrows(EmailAlreadyExistsException.class, () ->
                authService.register(CustomerType.NORMAL, "Nguyen Van B", "0901234567",
                        "taken@email.com", "secret123", null, null, null, 0));
        verify(customerService, never()).create(any());
    }

    @Test
    void register_weakPassword_throwsIllegalArgumentException() {
        AuthService authService = new AuthService(customerRepository, customerService);

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(CustomerType.NORMAL, "Nguyen Van A", "0901234567",
                        "a@email.com", "123", null, null, null, 0));
        verify(customerService, never()).create(any());
    }

    // ---- Tình huống: đăng nhập thành công ----
    @Test
    void login_correctCredentials_returnsCustomerWithoutPassword() {
        AuthService authService = new AuthService(customerRepository, customerService);
        Customer customer = new NormalCustomer("CUS001", "Nguyen Van A", "0901234567", "a@email.com");
        customer.setPassword(PasswordUtils.hash("secret123"));
        when(customerRepository.findByEmail("a@email.com")).thenReturn(customer);

        Customer result = authService.login("a@email.com", "secret123");

        assertEquals("CUS001", result.getUserId());
        assertNull(result.getPassword());
    }

    // ---- Tình huống: sai mật khẩu ----
    @Test
    void login_wrongPassword_throwsAuthenticationException() {
        AuthService authService = new AuthService(customerRepository, customerService);
        Customer customer = new NormalCustomer("CUS001", "Nguyen Van A", "0901234567", "a@email.com");
        customer.setPassword(PasswordUtils.hash("secret123"));
        when(customerRepository.findByEmail("a@email.com")).thenReturn(customer);

        assertThrows(AuthenticationException.class, () -> authService.login("a@email.com", "wrongpass"));
    }

    @Test
    void login_unknownEmail_throwsAuthenticationException() {
        AuthService authService = new AuthService(customerRepository, customerService);
        when(customerRepository.findByEmail("ghost@email.com")).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> authService.login("ghost@email.com", "whatever"));
    }
}
