package service;

import exception.PaymentFailedException;
import model.Ticket;

/** Thanh toán tiền mặt: luôn thành công. */
public class CashPayment implements IPaymentMethod {
    @Override
    public void processPayment(Ticket ticket, double amount) throws PaymentFailedException {
        // Tiền mặt thu trực tiếp tại quầy, không có rủi ro giao dịch -> luôn thành công.
    }
}
