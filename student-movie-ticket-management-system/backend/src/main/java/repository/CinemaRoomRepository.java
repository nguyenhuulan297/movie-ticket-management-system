package repository;

import model.CinemaRoom;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CinemaRoomRepository {
    private static final String DEFAULT_FILE_PATH = "data/cinemarooms.json";

    private final String filePath;
    private final Gson gson = new Gson();

    public CinemaRoomRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/cinemarooms.json).
     */
    public CinemaRoomRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<CinemaRoom> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<CinemaRoom>>(){}.getType();
        List<CinemaRoom> rooms = gson.fromJson(json, listType);
        return rooms != null ? rooms : new ArrayList<>();
    }

    public CinemaRoom findById(String roomId) {
        return findAll().stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst()
                .orElse(null);
    }

    public void save(CinemaRoom room) {
        List<CinemaRoom> rooms = findAll();
        rooms.add(room);
        FileUtils.writeJsonFile(filePath, gson.toJson(rooms));
    }

    public void update(CinemaRoom room) {
        List<CinemaRoom> rooms = findAll();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId().equals(room.getRoomId())) {
                rooms.set(i, room);
                break;
            }
        }
        FileUtils.writeJsonFile(filePath, gson.toJson(rooms));
    }

    public void deleteById(String roomId) {
        List<CinemaRoom> rooms = findAll();
        rooms.removeIf(r -> r.getRoomId().equals(roomId));
        FileUtils.writeJsonFile(filePath, gson.toJson(rooms));
    }
}
