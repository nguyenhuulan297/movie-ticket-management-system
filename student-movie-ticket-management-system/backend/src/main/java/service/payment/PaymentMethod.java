package service.payment;

import exception.PaymentFailedException;

public interface PaymentMethod {
    boolean processPayment(double amount) throws PaymentFailedException;
    String getMethodName();
}