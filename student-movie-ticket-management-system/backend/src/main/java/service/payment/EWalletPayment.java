package service.payment;

import exception.PaymentFailedException;

import java.util.Random;

public class EWalletPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) throws PaymentFailedException {
        boolean success = new Random().nextDouble() > 0.15;

        if (!success) {
            throw new PaymentFailedException("Thanh toán ví điện tử thất bại. Vui lòng thử lại.");
        }

        System.out.println("📱 Thanh toán VÍ ĐIỆN TỬ: " + amount + " VND → Thành công");
        return true;
    }

    @Override
    public String getMethodName() {
        return "EWALLET";
    }
}