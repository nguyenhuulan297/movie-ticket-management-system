package service;

import exception.PaymentFailedException;
import model.Ticket;
import model.PaymentStatus;

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
        boolean success = random.nextDouble() < 0.8;
        if (success) {
            ticket.setPaymentStatus(PaymentStatus.PAID);
            System.out.println("Thanh toán ví điện tử thành công cho vé: " + ticket.getTicketId());
        } else {
            ticket.setPaymentStatus(PaymentStatus.FAILED);
            throw new PaymentFailedException("Giao dịch ví điện tử thất bại, vui lòng thử lại");
        }
    }
}
