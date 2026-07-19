package service;

import model.Payment;
import model.Ticket;
import repository.PaymentRepository;
import service.payment.*;
import exception.PaymentFailedException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Xử lý thanh toán cho vé
     */
    public Payment processPayment(Ticket ticket, String method) throws PaymentFailedException {
        double amount = ticket.getFinalPrice();

        PaymentMethod paymentMethod = switch (method.toUpperCase()) {
            case "CASH" -> new CashPayment();
            case "BANK_TRANSFER" -> new BankTransferPayment();
            case "EWALLET" -> new EWalletPayment();
            default -> throw new IllegalArgumentException("Phương thức thanh toán không hỗ trợ: " + method);
        };

        boolean success = paymentMethod.processPayment(amount);

        Payment payment = new Payment(ticket, method, amount);
        payment.setSuccess(success);

        paymentRepository.save(payment);

        // Cập nhật trạng thái vé
        if (success) {
            ticket.setPaymentStatus(PaymentStatus.PAID);
        } else {
            ticket.setPaymentStatus(PaymentStatus.FAILED);
        }

        return payment;
    }
}