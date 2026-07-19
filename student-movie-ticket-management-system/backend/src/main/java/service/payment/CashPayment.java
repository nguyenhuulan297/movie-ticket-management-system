package service.payment;

import exception.PaymentFailedException;

public class CashPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("💵 Thanh toán bằng TIỀN MẶT: " + amount + " VND → Thành công");
        return true;
    }

    @Override
    public String getMethodName() {
        return "CASH";
    }
}