package service;

import exception.TicketNotFoundException;
import model.Payment;
import model.PaymentStatus;
import model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.PaymentRepository;
import repository.TicketRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private Ticket pendingTicket(String ticketId, double price) {
        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setFinalPrice(price);
        ticket.setPaymentStatus(PaymentStatus.PENDING);
        return ticket;
    }

    @Test
    void pay_ticketNotFound_throwsTicketNotFoundException() {
        PaymentService service = new PaymentService(ticketRepository, paymentRepository);
        when(ticketRepository.findById("TKT999")).thenReturn(null);

        assertThrows(TicketNotFoundException.class, () -> service.pay("TKT999", "CASH"));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void pay_ticketAlreadyPaid_throwsIllegalStateException() {
        PaymentService service = new PaymentService(ticketRepository, paymentRepository);
        Ticket ticket = pendingTicket("TKT001", 80000);
        ticket.setPaymentStatus(PaymentStatus.PAID);
        when(ticketRepository.findById("TKT001")).thenReturn(ticket);

        assertThrows(IllegalStateException.class, () -> service.pay("TKT001", "CASH"));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void pay_invalidMethod_throwsIllegalArgumentException() {
        PaymentService service = new PaymentService(ticketRepository, paymentRepository);
        Ticket ticket = pendingTicket("TKT001", 80000);
        when(ticketRepository.findById("TKT001")).thenReturn(ticket);

        assertThrows(IllegalArgumentException.class, () -> service.pay("TKT001", "BITCOIN"));
    }

    // ---- Tình huống: thanh toán tiền mặt luôn thành công ----
    @Test
    void pay_withCash_alwaysSucceedsAndMarksTicketPaid() {
        PaymentService service = new PaymentService(ticketRepository, paymentRepository);
        Ticket ticket = pendingTicket("TKT001", 80000);
        when(ticketRepository.findById("TKT001")).thenReturn(ticket);
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment payment = service.pay("TKT001", "CASH");

        assertTrue(payment.isSuccess());
        assertEquals(PaymentStatus.PAID, ticket.getPaymentStatus());
        assertEquals(80000, payment.getAmount());
        verify(ticketRepository).update(ticket);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void pay_methodNameIsCaseInsensitive() {
        PaymentService service = new PaymentService(ticketRepository, paymentRepository);
        Ticket ticket = pendingTicket("TKT001", 80000);
        when(ticketRepository.findById("TKT001")).thenReturn(ticket);
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment payment = service.pay("TKT001", "cash");

        assertTrue(payment.isSuccess());
    }
}
