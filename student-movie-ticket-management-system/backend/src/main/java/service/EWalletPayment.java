package service;

import exception.PaymentFailedException;
import model.Ticket;

import java.util.Random;

/** Thanh toán ví điện tử: mô phỏng tỉ lệ thành công 80%. */
public class EWalletPayment implements IPaymentMethod {
    private final Random random;

    public EWalletPayment() {
        this(new Random());
    }

    EWalletPayment(Random random) {
        this.random = random;
    }

    @Override
    public void processPayment(Ticket ticket, double amount) throws PaymentFailedException {
        if (random.nextDouble() >= 0.8) {
            throw new PaymentFailedException("Giao dịch ví điện tử thất bại, vui lòng thử lại");
        }
    }
}
