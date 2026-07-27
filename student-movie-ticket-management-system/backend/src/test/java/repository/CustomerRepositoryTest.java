package repository;

import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerRepositoryTest {

    /**
     * Đọc trực tiếp data/customers.json (dữ liệu có sẵn của dự án) - chỉ đọc,
     * không ghi, để không làm thay đổi file dữ liệu dùng chung.
     */
    @Test
    void findAll_readsExistingCustomersJsonWithCorrectSubclasses() {
        CustomerRepository repository = new CustomerRepository();

        List<Customer> customers = repository.findAll();

        assertEquals(5, customers.size());

        Customer student = repository.findById("CUS003");
        assertInstanceOf(StudentCustomer.class, student);
        assertEquals("SV123456", ((StudentCustomer) student).getStudentId());

        Customer vip = repository.findById("CUS005");
        assertInstanceOf(VIPCustomer.class, vip);
        assertEquals(350, ((VIPCustomer) vip).getPointBalance());

        Customer normal = repository.findById("CUS001");
        assertInstanceOf(NormalCustomer.class, normal);

        assertNull(repository.findById("CUS_NOT_EXIST"));
    }

    @Test
    void saveUpdateDelete_roundTripOnIsolatedTempFile(@TempDir Path tempDir) {
        String filePath = tempDir.resolve("customers-test.json").toString();
        CustomerRepository repository = new CustomerRepository(filePath);

        Customer customer = new NormalCustomer(null, "Test User", "0900000000", "test@email.com");
        Customer saved = repository.save(customer);

        assertNotNull(saved.getUserId());
        assertTrue(saved.getUserId().startsWith("CUS"));

        Customer found = repository.findById(saved.getUserId());
        assertNotNull(found);
        assertEquals("Test User", found.getFullName());

        found.setFullName("Updated Name");
        repository.update(found);
        assertEquals("Updated Name", repository.findById(saved.getUserId()).getFullName());

        boolean removed = repository.deleteById(saved.getUserId());
        assertTrue(removed);
        assertNull(repository.findById(saved.getUserId()));

        assertFalse(repository.deleteById("CUS_NOT_EXIST"));
    }

    @Test
    void save_assignsIdWhenMissingAndKeepsCustomerTypeSpecificFields(@TempDir Path tempDir) {
        String filePath = tempDir.resolve("customers-test-2.json").toString();
        CustomerRepository repository = new CustomerRepository(filePath);

        Customer student = new StudentCustomer(null, "Sinh Vien A", "0911111111",
                "sv@student.edu.vn", "SV999", "Truong ABC");
        repository.save(student);

        Customer reloaded = repository.findById(student.getUserId());
        assertInstanceOf(StudentCustomer.class, reloaded);
        assertEquals("SV999", ((StudentCustomer) reloaded).getStudentId());
        assertEquals(CustomerType.STUDENT, reloaded.getCustomerType());
    }
}
