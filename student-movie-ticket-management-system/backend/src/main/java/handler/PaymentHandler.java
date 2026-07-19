package handler;

import model.Payment;
import model.Ticket;
import service.PaymentService;
import exception.PaymentFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentHandler {

    private final PaymentService paymentService;

    public PaymentHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * API Thanh toán vé
     * POST /api/payments
     */
    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.processPayment(request.getTicket(), request.getMethod());
            return ResponseEntity.ok(payment);
        } catch (PaymentFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Thanh toán thất bại: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * DTO cho Request Body
     */
    static class PaymentRequest {
        private Ticket ticket;
        private String method;   // CASH, BANK_TRANSFER, EWALLET

        public Ticket getTicket() { return ticket; }
        public void setTicket(Ticket ticket) { this.ticket = ticket; }

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }
}