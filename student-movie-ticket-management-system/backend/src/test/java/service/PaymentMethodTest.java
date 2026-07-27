package service;

import exception.PaymentFailedException;
import model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMethodTest {

    @Mock
    private Random random;

    @Test
    void cashPayment_alwaysSucceeds() {
        CashPayment cashPayment = new CashPayment();
        assertDoesNotThrow(() -> cashPayment.processPayment(new Ticket(), 80000));
    }

    // ---- Tình huống: chuyển khoản thành công (tỉ lệ 80%) ----
    @Test
    void bankTransferPayment_belowThreshold_succeeds() {
        when(random.nextDouble()).thenReturn(0.79);
        BankTransferPayment payment = new BankTransferPayment(random);

        assertDoesNotThrow(() -> payment.processPayment(new Ticket(), 80000));
    }

    // ---- Tình huống: chuyển khoản thất bại ----
    @Test
    void bankTransferPayment_atOrAboveThreshold_throwsPaymentFailedException() {
        when(random.nextDouble()).thenReturn(0.8);
        BankTransferPayment payment = new BankTransferPayment(random);

        assertThrows(PaymentFailedException.class, () -> payment.processPayment(new Ticket(), 80000));
    }

    @Test
    void eWalletPayment_belowThreshold_succeeds() {
        when(random.nextDouble()).thenReturn(0.1);
        EWalletPayment payment = new EWalletPayment(random);

        assertDoesNotThrow(() -> payment.processPayment(new Ticket(), 80000));
    }

    @Test
    void eWalletPayment_atOrAboveThreshold_throwsPaymentFailedException() {
        when(random.nextDouble()).thenReturn(0.99);
        EWalletPayment payment = new EWalletPayment(random);

        assertThrows(PaymentFailedException.class, () -> payment.processPayment(new Ticket(), 80000));
    }
}
