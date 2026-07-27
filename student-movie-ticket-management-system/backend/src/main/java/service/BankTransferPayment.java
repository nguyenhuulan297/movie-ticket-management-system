package service;

import exception.PaymentFailedException;
import model.Ticket;

import java.util.Random;

/** Thanh toán chuyển khoản: mô phỏng tỉ lệ thành công 80%. */
public class BankTransferPayment implements IPaymentMethod {
    private final Random random;

    public BankTransferPayment() {
        this(new Random());
    }

    BankTransferPayment(Random random) {
        this.random = random;
    }

    @Override
    public void processPayment(Ticket ticket, double amount) throws PaymentFailedException {
        if (random.nextDouble() >= 0.8) {
            throw new PaymentFailedException("Giao dịch chuyển khoản thất bại, vui lòng thử lại");
        }
    }
}
