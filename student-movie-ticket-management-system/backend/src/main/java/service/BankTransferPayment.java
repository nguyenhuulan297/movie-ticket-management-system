package service;

import exception.PaymentFailedException;
import model.Ticket;
import model.PaymentStatus;

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
        boolean success = random.nextDouble() < 0.8; // 80% thành công
        if (success) {
            ticket.setPaymentStatus(PaymentStatus.PAID);
            System.out.println("Chuyển khoản thành công cho vé: " + ticket.getTicketId());
        } else {
            ticket.setPaymentStatus(PaymentStatus.FAILED);
            throw new PaymentFailedException("Giao dịch chuyển khoản thất bại, vui lòng thử lại");
        }
    }
}