package service;

import exception.PaymentFailedException;
import model.Ticket;
import model.PaymentStatus;

/** Thanh toán tiền mặt: luôn thành công. */
public class CashPayment implements IPaymentMethod {
    @Override
    public void processPayment(Ticket ticket, double amount) throws PaymentFailedException {
        // Tiền mặt thu trực tiếp tại quầy, không có rủi ro giao dịch -> luôn thành công.
        ticket.setPaymentStatus(PaymentStatus.PAID);
        System.out.println("Thanh toán tiền mặt thành công cho vé: " + ticket.getTicketId());
    }
}
