package service;

import exception.PaymentFailedException;
import exception.TicketNotFoundException;
import model.Payment;
import model.PaymentStatus;
import model.Ticket;
import org.springframework.stereotype.Service;
import repository.PaymentRepository;
import repository.TicketRepository;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(TicketRepository ticketRepository, PaymentRepository paymentRepository) {
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Xử lý thanh toán cho 1 vé. Theo đúng phân công: module Payment gọi thẳng vào
     * TicketRepository (không qua BookingService).
     */
    public Payment pay(String ticketId, String method) {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Không tìm thấy vé với ID: " + ticketId);
        }
        if (ticket.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Vé " + ticketId + " đã được thanh toán trước đó");
        }

        IPaymentMethod paymentMethod = resolveMethod(method);

        Payment payment = new Payment();
        payment.setTicketId(ticketId);
        payment.setMethod(method);
        payment.setAmount(ticket.getFinalPrice());
        payment.setPaidAt(LocalDateTime.now().toString());

        try {
            paymentMethod.processPayment(ticket, ticket.getFinalPrice());
            payment.setSuccess(true);
            ticket.setPaymentStatus(PaymentStatus.PAID);
            ticketRepository.update(ticket);
        } catch (PaymentFailedException e) {
            // Thanh toán thất bại: vé giữ nguyên trạng thái PENDING (chưa thanh toán),
            // ghế vẫn được giữ để khách có thể thử lại phương thức khác.
            payment.setSuccess(false);
            payment.setFailureReason(e.getMessage());
        }

        return paymentRepository.save(payment);
    }

    private IPaymentMethod resolveMethod(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống");
        }
        switch (method.toUpperCase()) {
            case "CASH": return new CashPayment();
            case "BANK_TRANSFER": return new BankTransferPayment();
            case "E_WALLET": return new EWalletPayment();
            default: throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + method);
        }
    }
}
