package handler;

import model.Booking;
import service.BookingService;
import exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingHandler {

    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> bookTickets(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.bookTickets(
                    request.getCustomerId(),
                    request.getShowtimeId(),
                    request.getSeatIds()
            );
            return ResponseEntity.ok(booking);
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
        private List<String> seatIds;   // <-- nhiều ghế cho 1 lần đặt

        public String getCustomerId() { return customerId; }
        public void setCustomerId(String customerId) { this.customerId = customerId; }
        public String getShowtimeId() { return showtimeId; }
        public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
        public List<String> getSeatIds() { return seatIds; }
        public void setSeatIds(List<String> seatIds) { this.seatIds = seatIds; }
    }
}