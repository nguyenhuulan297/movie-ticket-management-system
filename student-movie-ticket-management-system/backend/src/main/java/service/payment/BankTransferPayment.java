package service.payment;

import exception.PaymentFailedException;

import java.util.Random;

public class BankTransferPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) throws PaymentFailedException {
        boolean success = new Random().nextDouble() > 0.2;
        if (!success) {
            throw new PaymentFailedException("Chuyển khoản ngân hàng thất bại. Vui lòng thử lại.");
        }

        System.out.println("🏦 Chuyển khoản NGÂN HÀNG: " + amount + " VND → Thành công");
        return true;
    }

    @Override
    public String getMethodName() {
        return "BANK_TRANSFER";
    }
}