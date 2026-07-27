package handler;

import model.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.TicketRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketHandler {

    private final TicketRepository ticketRepository;

    public TicketHandler(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets(@RequestParam(required = false) String customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(ticketRepository.findByCustomerId(customerId));
        }
        return ResponseEntity.ok(ticketRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy vé với ID: " + id);
        }
        return ResponseEntity.ok(ticket);
    }
}
