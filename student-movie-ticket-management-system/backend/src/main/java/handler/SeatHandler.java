package handler;

import model.Seat;
import service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatHandler {

    private final SeatService seatService;

    public SeatHandler(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getSeatsByShowtime(@RequestParam String showtimeId) {
        return ResponseEntity.ok(seatService.findByShowtimeId(showtimeId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestParam String showtimeId) {
        return ResponseEntity.ok(seatService.getAvailableSeats(showtimeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable String id) {
        Seat seat = seatService.findById(id);
        if (seat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ghế với ID: " + id);
        }
        return ResponseEntity.ok(seat);
    }
}
