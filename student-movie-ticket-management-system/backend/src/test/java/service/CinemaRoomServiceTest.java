package service;

import model.CinemaRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.CinemaRoomRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CinemaRoomServiceTest {

    @TempDir
    Path tempDir;

    private CinemaRoomService cinemaRoomService;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("cinemarooms.json");
        Files.writeString(dataFile, "[]");
        CinemaRoomRepository repository = new CinemaRoomRepository(dataFile.toString());
        cinemaRoomService = new CinemaRoomService(repository);
    }

    @Test
    void create_validRoom_generatesIdAndPersists() {
        CinemaRoom room = new CinemaRoom(null, "Phòng chiếu số 1", 50);
        CinemaRoom created = cinemaRoomService.create(room);

        assertNotNull(created.getRoomId());
        assertEquals(created.getRoomId(), cinemaRoomService.findById(created.getRoomId()).getRoomId());
    }

    @Test
    void create_zeroTotalSeats_throwsIllegalArgumentException() {
        CinemaRoom room = new CinemaRoom(null, "Phòng lỗi", 0);
        assertThrows(IllegalArgumentException.class, () -> cinemaRoomService.create(room));
    }

    @Test
    void create_negativeTotalSeats_throwsIllegalArgumentException() {
        CinemaRoom room = new CinemaRoom(null, "Phòng lỗi", -5);
        assertThrows(IllegalArgumentException.class, () -> cinemaRoomService.create(room));
    }

    @Test
    void findById_nonExistingRoom_returnsNull() {
        assertNull(cinemaRoomService.findById("RM999"));
    }

    @Test
    void update_existingRoom_updatesSuccessfully() {
        CinemaRoom created = cinemaRoomService.create(new CinemaRoom(null, "Phòng cũ", 30));

        CinemaRoom updateData = new CinemaRoom(created.getRoomId(), "Phòng mới", 60);
        CinemaRoom updated = cinemaRoomService.update(updateData);

        assertNotNull(updated);
        assertEquals("Phòng mới", cinemaRoomService.findById(created.getRoomId()).getRoomName());
        assertEquals(60, cinemaRoomService.findById(created.getRoomId()).getTotalSeats());
    }

    @Test
    void update_nonExistingRoom_returnsNull() {
        CinemaRoom updateData = new CinemaRoom("RM999", "Không tồn tại", 20);
        assertNull(cinemaRoomService.update(updateData));
    }

    @Test
    void deleteById_removesRoom() {
        CinemaRoom created = cinemaRoomService.create(new CinemaRoom(null, "Phòng để xóa", 20));
        cinemaRoomService.deleteById(created.getRoomId());

        assertNull(cinemaRoomService.findById(created.getRoomId()));
    }
}
