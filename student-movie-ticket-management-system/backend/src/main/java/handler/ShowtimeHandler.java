package handler;

import model.Showtime;
import service.ShowtimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeHandler {

    private final ShowtimeService showtimeService;

    public ShowtimeHandler(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimes(@RequestParam(required = false) String movieId) {
        if (movieId != null) {
            return ResponseEntity.ok(showtimeService.findByMovieId(movieId));
        }
        return ResponseEntity.ok(showtimeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowtimeById(@PathVariable String id) {
        Showtime showtime = showtimeService.findById(id);
        if (showtime == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy suất chiếu với ID: " + id);
        }
        return ResponseEntity.ok(showtime);
    }
}
