package repository;

import model.Seat;
import model.SeatStatus;
import model.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatRepositoryTest {

    @TempDir
    Path tempDir;

    private SeatRepository seatRepository;
    private Path dataFile;

    @BeforeEach
    void setUp() throws IOException {
        dataFile = tempDir.resolve("seats.json");
        String seedJson = "[" +
                "{\"seatId\":\"SEAT001\",\"seatNumber\":\"A1\",\"seatType\":\"NORMAL\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT002\",\"seatNumber\":\"A2\",\"seatType\":\"NORMAL\",\"status\":\"BOOKED\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT003\",\"seatNumber\":\"B1\",\"seatType\":\"VIP\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT004\",\"seatNumber\":\"A1\",\"seatType\":\"NORMAL\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST002\"}" +
                "]";
        Files.writeString(dataFile, seedJson);
        seatRepository = new SeatRepository(dataFile.toString());
    }

    @Test
    void findAll_returnsAllSeatsFromFile() {
        List<Seat> seats = seatRepository.findAll();
        assertEquals(4, seats.size());
    }

    @Test
    void findById_existingSeat_returnsSeat() {
        Seat seat = seatRepository.findById("SEAT001");
        assertNotNull(seat);
        assertEquals("A1", seat.getSeatNumber());
        assertEquals(SeatType.NORMAL, seat.getSeatType());
    }

    @Test
    void findById_nonExistingSeat_returnsNull() {
        assertNull(seatRepository.findById("SEAT999"));
    }

    @Test
    void findByShowtimeId_filtersOnlySeatsOfThatShowtime() {
        List<Seat> seats = seatRepository.findByShowtimeId("ST001");
        assertEquals(3, seats.size());
        assertTrue(seats.stream().allMatch(s -> s.getShowtimeId().equals("ST001")));
    }

    @Test
    void findAvailableSeats_excludesBookedSeats() {
        List<Seat> available = seatRepository.findAvailableSeats("ST001");
        assertEquals(2, available.size());
        assertTrue(available.stream().noneMatch(s -> s.getSeatId().equals("SEAT002")));
    }

    @Test
    void update_persistsChangeBackToFile() throws IOException {
        Seat seat = seatRepository.findById("SEAT001");
        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.update(seat);

        // đọc lại từ file thay vì cache, đảm bảo ghi đĩa thật sự xảy ra
        SeatRepository freshRepository = new SeatRepository(dataFile.toString());
        Seat reloaded = freshRepository.findById("SEAT001");
        assertEquals(SeatStatus.BOOKED, reloaded.getStatus());
    }
}
