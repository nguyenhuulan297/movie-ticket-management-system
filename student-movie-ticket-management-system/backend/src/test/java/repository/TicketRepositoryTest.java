package repository;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketRepositoryTest {

    @TempDir
    Path tempDir;

    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("tickets.json");
        Files.writeString(dataFile, "[]");
        ticketRepository = new TicketRepository(dataFile.toString());
    }

    private Ticket newTicket(String ticketId, Customer customer) {
        Showtime showtime = new Showtime("ST001", "MOV001",
                new CinemaRoom("RM001", "Phòng 1", 50),
                "2026-07-05 14:00:00", "2026-07-05 17:00:00", 80000);
        Seat seat = new Seat("SEAT001", "A1", SeatType.NORMAL, SeatStatus.BOOKED, "ST001");
        return new Ticket(ticketId, customer, showtime, seat, 80000, PaymentStatus.PENDING, "2026-07-05T10:00:00");
    }

    // ---- Tình huống: Ticket chứa Customer đa hình (Student/VIP) phải đọc lại đúng subclass ----
    @Test
    void findById_ticketWithStudentCustomer_deserializesCorrectSubclass() {
        Customer student = new StudentCustomer("CUS003", "Le Minh C", "0923456789",
                "c@student.edu.vn", "SV123456", "Bach Khoa");
        ticketRepository.save(newTicket("TKT001", student));

        Ticket found = ticketRepository.findById("TKT001");

        assertNotNull(found);
        assertInstanceOf(StudentCustomer.class, found.getCustomer());
        assertEquals("SV123456", ((StudentCustomer) found.getCustomer()).getStudentId());
    }

    @Test
    void findById_ticketWithVipCustomer_deserializesCorrectSubclass() {
        Customer vip = new VIPCustomer("CUS005", "Hoang Van E", "0945678901",
                "e@vip.com", "2024-01-15", 350);
        ticketRepository.save(newTicket("TKT002", vip));

        Ticket found = ticketRepository.findById("TKT002");

        assertInstanceOf(VIPCustomer.class, found.getCustomer());
        assertEquals(350, ((VIPCustomer) found.getCustomer()).getPointBalance());
    }

    @Test
    void findByCustomerId_returnsOnlyTicketsOfThatCustomer() {
        Customer a = new NormalCustomer("CUS001", "A", "0900000001", "a@email.com");
        Customer b = new NormalCustomer("CUS002", "B", "0900000002", "b@email.com");
        ticketRepository.save(newTicket("TKT001", a));
        ticketRepository.save(newTicket("TKT002", b));
        ticketRepository.save(newTicket("TKT003", a));

        List<Ticket> ticketsOfA = ticketRepository.findByCustomerId("CUS001");

        assertEquals(2, ticketsOfA.size());
    }

    @Test
    void update_changesPaymentStatusAndPersists() {
        Customer customer = new NormalCustomer("CUS001", "A", "0900000001", "a@email.com");
        Ticket ticket = newTicket("TKT001", customer);
        ticketRepository.save(ticket);

        ticket.setPaymentStatus(PaymentStatus.PAID);
        ticketRepository.update(ticket);

        assertEquals(PaymentStatus.PAID, ticketRepository.findById("TKT001").getPaymentStatus());
    }
}
