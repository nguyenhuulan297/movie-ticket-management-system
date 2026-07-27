package handler;

import exception.TicketNotFoundException;
import model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentHandler {

    private final PaymentService paymentService;

    public PaymentHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> pay(@RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.pay(request.getTicketId(), request.getMethod());
            // success=false là kết quả nghiệp vụ hợp lệ (giao dịch bị từ chối), không phải lỗi hệ thống -> vẫn trả 200.
            return ResponseEntity.ok(payment);
        } catch (TicketNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    static class PaymentRequest {
        private String ticketId;
        private String method;

        public String getTicketId() { return ticketId; }
        public void setTicketId(String ticketId) { this.ticketId = ticketId; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }
}
