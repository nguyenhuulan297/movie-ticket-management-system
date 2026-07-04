package service;

import model.Ticket;
import exception.PaymentFailedException;

public interface IPaymentMethod {
    void processPayment(Ticket ticket, double amount) throws PaymentFailedException;

}
