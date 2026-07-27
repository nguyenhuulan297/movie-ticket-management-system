package repository;

import model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    @TempDir
    Path tempDir;

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("payments.json");
        Files.writeString(dataFile, "[]");
        paymentRepository = new PaymentRepository(dataFile.toString());
    }

    @Test
    void save_assignsGeneratedPaymentId() {
        Payment payment = new Payment(null, "TKT001", "CASH", 80000, "2026-07-05T10:00:00", true, null);

        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved.getPaymentId());
        assertTrue(saved.getPaymentId().startsWith("PAY"));
    }

    @Test
    void findById_returnsSavedPayment() {
        Payment saved = paymentRepository.save(
                new Payment(null, "TKT001", "CASH", 80000, "2026-07-05T10:00:00", true, null));

        Payment found = paymentRepository.findById(saved.getPaymentId());

        assertNotNull(found);
        assertEquals("TKT001", found.getTicketId());
        assertTrue(found.isSuccess());
    }

    @Test
    void findByTicketId_returnsOnlyPaymentsForThatTicket() {
        paymentRepository.save(new Payment(null, "TKT001", "CASH", 80000, "t1", true, null));
        paymentRepository.save(new Payment(null, "TKT001", "E_WALLET", 80000, "t2", false, "Thất bại"));
        paymentRepository.save(new Payment(null, "TKT002", "CASH", 90000, "t3", true, null));

        List<Payment> paymentsForTkt001 = paymentRepository.findByTicketId("TKT001");

        assertEquals(2, paymentsForTkt001.size());
    }

    @Test
    void findById_unknownId_returnsNull() {
        assertNull(paymentRepository.findById("PAY_NOT_EXIST"));
    }
}
