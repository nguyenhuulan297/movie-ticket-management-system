package handler;

import model.Ticket;
import service.BookingService;
import exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingHandler {

    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        try {
            Ticket ticket = bookingService.bookTicket(
                    request.getCustomerId(),
                    request.getShowtimeId(),
                    request.getSeatId(),
                    request.getPaymentType()
            );
            return ResponseEntity.ok(ticket);
        } catch (CustomerNotFoundException | ShowtimeNotFoundException | SeatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SeatAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (MovieNotShowingException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // DTO cho request body
    static class BookingRequest {
        private String customerId;
        private String showtimeId;
        private String seatId;
        private String paymentType;

        public String getCustomerId() { return customerId; }
        public void setCustomerId(String customerId) { this.customerId = customerId; }

        public String getShowtimeId() { return showtimeId; }
        public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }

        public String getSeatId() { return seatId; }
        public void setSeatId(String seatId) { this.seatId = seatId; }

        public String getPaymentType() { return paymentType; }
        public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    }
}