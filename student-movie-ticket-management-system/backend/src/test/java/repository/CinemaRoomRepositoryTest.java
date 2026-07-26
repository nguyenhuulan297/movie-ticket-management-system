package repository;

import model.CinemaRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CinemaRoomRepositoryTest {

    @TempDir
    Path tempDir;

    private CinemaRoomRepository cinemaRoomRepository;
    private Path dataFile;

    @BeforeEach
    void setUp() throws IOException {
        dataFile = tempDir.resolve("cinemarooms.json");
        Files.writeString(dataFile, "[]");
        cinemaRoomRepository = new CinemaRoomRepository(dataFile.toString());
    }

    @Test
    void save_then_findById_returnsSameRoom() {
        CinemaRoom room = new CinemaRoom("RM001", "Phòng chiếu số 1", 50);
        cinemaRoomRepository.save(room);

        CinemaRoom found = cinemaRoomRepository.findById("RM001");
        assertNotNull(found);
        assertEquals("Phòng chiếu số 1", found.getRoomName());
        assertEquals(50, found.getTotalSeats());
    }

    @Test
    void findAll_returnsEveryStoredRoom() {
        cinemaRoomRepository.save(new CinemaRoom("RM001", "Phòng 1", 50));
        cinemaRoomRepository.save(new CinemaRoom("RM002", "Phòng 2", 40));

        List<CinemaRoom> rooms = cinemaRoomRepository.findAll();
        assertEquals(2, rooms.size());
    }

    @Test
    void update_changesRoomFieldsAndPersists() {
        cinemaRoomRepository.save(new CinemaRoom("RM001", "Phòng cũ", 30));

        CinemaRoom updated = new CinemaRoom("RM001", "Phòng mới", 45);
        cinemaRoomRepository.update(updated);

        CinemaRoom found = cinemaRoomRepository.findById("RM001");
        assertEquals("Phòng mới", found.getRoomName());
        assertEquals(45, found.getTotalSeats());
    }

    @Test
    void deleteById_removesRoomFromStorage() {
        cinemaRoomRepository.save(new CinemaRoom("RM001", "Phòng 1", 50));
        cinemaRoomRepository.deleteById("RM001");

        assertNull(cinemaRoomRepository.findById("RM001"));
        assertTrue(cinemaRoomRepository.findAll().isEmpty());
    }
}
