package repository;

import model.Seat;
import model.SeatStatus;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SeatRepository {
    private static final String DEFAULT_FILE_PATH = "data/seats.json";
    private final String filePath;
    private final Gson gson = new Gson();

    public SeatRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/seats.json).
     */
    public SeatRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Seat> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Seat>>(){}.getType();
        List<Seat> seats = gson.fromJson(json, listType);
        return seats != null ? seats : new ArrayList<>();
    }

    public Seat findById(String seatId) {
        return findAll().stream()
                .filter(s -> s.getSeatId().equals(seatId))
                .findFirst()
                .orElse(null);
    }

    public List<Seat> findByShowtimeId(String showtimeId) {
        return findAll().stream()
                .filter(s -> s.getShowtimeId().equals(showtimeId))
                .collect(Collectors.toList());
    }

    public List<Seat> findAvailableSeats(String showtimeId) {
        return findAll().stream()
                .filter(s -> s.getShowtimeId().equals(showtimeId))
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    public void update(Seat seat) {
        List<Seat> seats = findAll();
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getSeatId().equals(seat.getSeatId())) {
                seats.set(i, seat);
                break;
            }
        }
        FileUtils.writeJsonFile(filePath, gson.toJson(seats));
    }
}