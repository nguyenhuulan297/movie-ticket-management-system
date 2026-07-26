package service;

import exception.CustomerNotFoundException;
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void findById_delegatesToRepository() {
        CustomerService service = new CustomerService(customerRepository);
        Customer customer = new NormalCustomer("CUS001", "Nguyen Van A", "0901234567", "a@email.com");
        when(customerRepository.findById("CUS001")).thenReturn(customer);

        Customer result = service.findById("CUS001");

        assertEquals(customer, result);
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        CustomerService service = new CustomerService(customerRepository);
        when(customerRepository.findById("CUS999")).thenReturn(null);

        assertEquals(null, service.findById("CUS999"));
    }

    @Test
    void create_rejectsInvalidPhoneAndDoesNotSave() {
        CustomerService service = new CustomerService(customerRepository);
        Customer customer = new NormalCustomer(null, "Nguyen Van A", "123", "a@email.com");

        assertThrows(IllegalArgumentException.class, () -> service.create(customer));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void create_rejectsStudentWithoutStudentId() {
        CustomerService service = new CustomerService(customerRepository);
        Customer customer = new StudentCustomer(null, "Le Minh C", "0923456789", "c@student.edu.vn", "", "Truong");

        assertThrows(IllegalArgumentException.class, () -> service.create(customer));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void create_savesValidCustomer() {
        CustomerService service = new CustomerService(customerRepository);
        Customer customer = new NormalCustomer(null, "Nguyen Van A", "0901234567", "a@email.com");
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = service.create(customer);

        assertEquals(customer, result);
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteById_throwsWhenCustomerNotFound() {
        CustomerService service = new CustomerService(customerRepository);
        when(customerRepository.deleteById("CUS999")).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> service.deleteById("CUS999"));
    }

    @Test
    void getPricePolicy_returnsMatchingPolicyPerCustomerType() {
        CustomerService service = new CustomerService(customerRepository);

        assertInstanceOf(NormalCustomerPricePolicy.class, service.getPricePolicy(CustomerType.NORMAL));
        assertInstanceOf(StudentPricePolicy.class, service.getPricePolicy(CustomerType.STUDENT));
        assertInstanceOf(VIPPricePolicy.class, service.getPricePolicy(CustomerType.VIP));
    }
}
