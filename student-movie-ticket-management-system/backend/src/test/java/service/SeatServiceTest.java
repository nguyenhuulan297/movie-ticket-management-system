package service;

import exception.SeatAlreadyBookedException;
import exception.SeatNotFoundException;
import model.Seat;
import model.SeatStatus;
import model.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.SeatRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatServiceTest {

    @TempDir
    Path tempDir;

    private SeatService seatService;
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("seats.json");
        String seedJson = "[" +
                "{\"seatId\":\"SEAT001\",\"seatNumber\":\"A1\",\"seatType\":\"NORMAL\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT002\",\"seatNumber\":\"A2\",\"seatType\":\"NORMAL\",\"status\":\"BOOKED\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT003\",\"seatNumber\":\"B1\",\"seatType\":\"VIP\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST001\"}" +
                "]";
        Files.writeString(dataFile, seedJson);
        seatRepository = new SeatRepository(dataFile.toString());
        seatService = new SeatService(seatRepository);
    }

    // ---- Tình huống 1: Khách đặt ghế còn trống -> thành công ----
    @Test
    void bookSeat_availableSeat_marksSeatAsBookedAndPersists() {
        seatService.bookSeat("SEAT001");

        Seat seat = seatRepository.findById("SEAT001");
        assertEquals(SeatStatus.BOOKED, seat.getStatus());
        assertFalse(seatService.isAvailable("SEAT001"));
    }

    // ---- Tình huống 2: Khách đặt ghế đã có người đặt -> lỗi ----
    @Test
    void bookSeat_alreadyBookedSeat_throwsSeatAlreadyBookedException() {
        assertThrows(SeatAlreadyBookedException.class, () -> seatService.bookSeat("SEAT002"));
    }

    // ---- Tình huống 3: Khách chọn ghế không tồn tại -> lỗi ----
    @Test
    void bookSeat_nonExistentSeat_throwsSeatNotFoundException() {
        assertThrows(SeatNotFoundException.class, () -> seatService.bookSeat("SEAT999"));
    }

    @Test
    void isAvailable_availableSeat_returnsTrue() {
        assertTrue(seatService.isAvailable("SEAT001"));
    }

    @Test
    void isAvailable_bookedSeat_returnsFalse() {
        assertFalse(seatService.isAvailable("SEAT002"));
    }

    @Test
    void isAvailable_nonExistentSeat_returnsFalseInsteadOfThrowing() {
        assertFalse(seatService.isAvailable("SEAT999"));
    }

    @Test
    void findById_delegatesToRepository() {
        Seat seat = seatService.findById("SEAT003");
        assertNotNull(seat);
        assertEquals(SeatType.VIP, seat.getSeatType());
    }

    @Test
    void getAvailableSeats_excludesBookedSeatsForGivenShowtime() {
        List<Seat> available = seatService.getAvailableSeats("ST001");
        assertEquals(2, available.size());
        assertTrue(available.stream().allMatch(s -> s.getStatus() == SeatStatus.AVAILABLE));
    }

    @Test
    void bookSeat_doesNotAffectOtherSeats() {
        seatService.bookSeat("SEAT001");

        assertTrue(seatService.isAvailable("SEAT003"));
        assertEquals(SeatStatus.AVAILABLE, seatRepository.findById("SEAT003").getStatus());
    }
}
