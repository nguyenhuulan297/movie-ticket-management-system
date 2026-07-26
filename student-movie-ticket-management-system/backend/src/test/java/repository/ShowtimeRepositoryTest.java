package repository;

import model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowtimeRepositoryTest {

    @TempDir
    Path tempDir;

    private ShowtimeRepository showtimeRepository;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("showtimes.json");
        String seedJson = "[" +
                "{\"showtimeId\":\"ST001\",\"movieId\":\"MOV001\"," +
                "\"room\":{\"roomId\":\"RM001\",\"roomName\":\"Phòng chiếu số 1\",\"totalSeats\":50}," +
                "\"startTime\":\"2026-07-05 14:00:00\",\"endTime\":\"2026-07-05 17:01:00\",\"basePrice\":80000}," +
                "{\"showtimeId\":\"ST002\",\"movieId\":\"MOV001\"," +
                "\"room\":{\"roomId\":\"RM002\",\"roomName\":\"Phòng chiếu số 2\",\"totalSeats\":40}," +
                "\"startTime\":\"2026-07-05 19:00:00\",\"endTime\":\"2026-07-05 22:01:00\",\"basePrice\":90000}," +
                "{\"showtimeId\":\"ST003\",\"movieId\":\"MOV002\"," +
                "\"room\":{\"roomId\":\"RM001\",\"roomName\":\"Phòng chiếu số 1\",\"totalSeats\":50}," +
                "\"startTime\":\"2026-07-06 10:00:00\",\"endTime\":\"2026-07-06 12:28:00\",\"basePrice\":75000}" +
                "]";
        Files.writeString(dataFile, seedJson);
        showtimeRepository = new ShowtimeRepository(dataFile.toString());
    }

    @Test
    void findAll_returnsAllShowtimesFromFile() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        assertEquals(3, showtimes.size());
    }

    @Test
    void findById_existingShowtime_returnsShowtimeWithRoom() {
        Showtime showtime = showtimeRepository.findById("ST001");
        assertNotNull(showtime);
        assertEquals("MOV001", showtime.getMovieId());
        assertEquals(80000, showtime.getBasePrice());
        assertNotNull(showtime.getRoom());
        assertEquals("RM001", showtime.getRoom().getRoomId());
        assertEquals(50, showtime.getRoom().getTotalSeats());
    }

    @Test
    void findById_nonExistingShowtime_returnsNull() {
        assertNull(showtimeRepository.findById("ST999"));
    }

    @Test
    void findByMovieId_filtersOnlyShowtimesOfThatMovie() {
        List<Showtime> showtimes = showtimeRepository.findByMovieId("MOV001");
        assertEquals(2, showtimes.size());
        assertTrue(showtimes.stream().allMatch(s -> s.getMovieId().equals("MOV001")));
    }

    @Test
    void findByMovieId_noMatchingMovie_returnsEmptyList() {
        List<Showtime> showtimes = showtimeRepository.findByMovieId("MOV999");
        assertTrue(showtimes.isEmpty());
    }
}
