package handler;

import model.CinemaRoom;
import service.CinemaRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinema-rooms")
public class CinemaRoomHandler {

    private final CinemaRoomService cinemaRoomService;

    public CinemaRoomHandler(CinemaRoomService cinemaRoomService) {
        this.cinemaRoomService = cinemaRoomService;
    }

    @GetMapping
    public ResponseEntity<List<CinemaRoom>> getAllRooms() {
        return ResponseEntity.ok(cinemaRoomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable String id) {
        CinemaRoom room = cinemaRoomService.findById(id);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phòng chiếu với ID: " + id);
        }
        return ResponseEntity.ok(room);
    }

    @PostMapping
    public ResponseEntity<CinemaRoom> createRoom(@RequestBody CinemaRoom room) {
        CinemaRoom created = cinemaRoomService.create(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable String id, @RequestBody CinemaRoom room) {
        room.setRoomId(id);
        CinemaRoom updated = cinemaRoomService.update(room);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phòng chiếu với ID: " + id);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        cinemaRoomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
