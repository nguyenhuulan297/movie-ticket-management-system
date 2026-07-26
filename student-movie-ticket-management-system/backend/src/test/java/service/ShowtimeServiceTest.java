package service;

import model.Seat;
import model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.SeatRepository;
import repository.ShowtimeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowtimeServiceTest {

    @TempDir
    Path tempDir;

    private ShowtimeService showtimeService;

    @BeforeEach
    void setUp() throws IOException {
        Path showtimesFile = tempDir.resolve("showtimes.json");
        String showtimesJson = "[" +
                "{\"showtimeId\":\"ST001\",\"movieId\":\"MOV001\"," +
                "\"room\":{\"roomId\":\"RM001\",\"roomName\":\"Phòng chiếu số 1\",\"totalSeats\":50}," +
                "\"startTime\":\"2026-07-05 14:00:00\",\"endTime\":\"2026-07-05 17:01:00\",\"basePrice\":80000}," +
                "{\"showtimeId\":\"ST002\",\"movieId\":\"MOV004\"," +
                "\"room\":{\"roomId\":\"RM002\",\"roomName\":\"Phòng chiếu số 2\",\"totalSeats\":40}," +
                "\"startTime\":\"2026-07-05 19:00:00\",\"endTime\":\"2026-07-05 22:01:00\",\"basePrice\":90000}" +
                "]";
        Files.writeString(showtimesFile, showtimesJson);
        ShowtimeRepository showtimeRepository = new ShowtimeRepository(showtimesFile.toString());

        Path seatsFile = tempDir.resolve("seats.json");
        String seatsJson = "[" +
                "{\"seatId\":\"SEAT001\",\"seatNumber\":\"A1\",\"seatType\":\"NORMAL\",\"status\":\"AVAILABLE\",\"showtimeId\":\"ST001\"}," +
                "{\"seatId\":\"SEAT002\",\"seatNumber\":\"A2\",\"seatType\":\"NORMAL\",\"status\":\"BOOKED\",\"showtimeId\":\"ST002\"}" +
                "]";
        Files.writeString(seatsFile, seatsJson);
        SeatService seatService = new SeatService(new SeatRepository(seatsFile.toString()));

        Path moviesFile = tempDir.resolve("movies.json");
        String moviesJson = "[" +
                "{\"movieId\":\"MOV001\",\"title\":\"Avengers: Endgame\",\"status\":\"SHOWING\"}," +
                "{\"movieId\":\"MOV004\",\"title\":\"Cô gái đến từ hôm qua\",\"status\":\"STOPPED\"}" +
                "]";
        Files.writeString(moviesFile, moviesJson);

        showtimeService = new ShowtimeService(showtimeRepository, seatService, moviesFile.toString());
    }

    @Test
    void findById_existingShowtime_returnsShowtime() {
        Showtime showtime = showtimeService.findById("ST001");
        assertNotNull(showtime);
        assertEquals("MOV001", showtime.getMovieId());
    }

    @Test
    void findById_nonExistingShowtime_returnsNull() {
        assertNull(showtimeService.findById("ST999"));
    }

    @Test
    void findByMovieId_returnsOnlyShowtimesOfThatMovie() {
        List<Showtime> showtimes = showtimeService.findByMovieId("MOV001");
        assertEquals(1, showtimes.size());
        assertEquals("ST001", showtimes.get(0).getShowtimeId());
    }

    @Test
    void exists_existingShowtime_returnsTrue() {
        assertTrue(showtimeService.exists("ST001"));
    }

    @Test
    void exists_nonExistingShowtime_returnsFalse() {
        assertFalse(showtimeService.exists("ST999"));
    }

    // ---- Tình huống: suất chiếu thuộc phim đang chiếu ----
    @Test
    void isMovieShowing_movieCurrentlyShowing_returnsTrue() {
        Showtime showtime = showtimeService.findById("ST001");
        assertTrue(showtimeService.isMovieShowing(showtime));
    }

    // ---- Tình huống: đặt vé cho phim đã ngừng chiếu ----
    @Test
    void isMovieShowing_movieStopped_returnsFalse() {
        Showtime showtime = showtimeService.findById("ST002");
        assertFalse(showtimeService.isMovieShowing(showtime));
    }

    @Test
    void isMovieShowing_unknownMovie_returnsFalse() {
        Showtime showtime = new Showtime("ST999", "MOV999", null, "2026-07-05 14:00:00", "2026-07-05 16:00:00", 80000);
        assertFalse(showtimeService.isMovieShowing(showtime));
    }

    @Test
    void isMovieShowing_nullShowtime_returnsFalse() {
        assertFalse(showtimeService.isMovieShowing(null));
    }

    @Test
    void getAvailableSeats_delegatesToSeatService() {
        List<Seat> available = showtimeService.getAvailableSeats("ST001");
        assertEquals(1, available.size());
        assertEquals("SEAT001", available.get(0).getSeatId());
    }

    @Test
    void hasAvailableSeats_showtimeWithFreeSeats_returnsTrue() {
        assertTrue(showtimeService.hasAvailableSeats("ST001"));
    }

    @Test
    void hasAvailableSeats_showtimeFullyBooked_returnsFalse() {
        assertFalse(showtimeService.hasAvailableSeats("ST002"));
    }
}
