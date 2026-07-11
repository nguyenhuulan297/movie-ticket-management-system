package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Seat;
import utils.FileUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatRepository {
    private static final String FILE_PATH = "data/seats.json";
    private final Gson gson = new Gson();

    public List<Seat> findAll() {
        String json = FileUtils.readJsonFile(FILE_PATH);
        Type listType = new TypeToken<List<Seat>>() {}.getType();
        List<Seat> seats = gson.fromJson(json, listType);
        return seats != null ? seats : new ArrayList<>();
    }

    public Seat findById(String id) {
        return findAll().stream()
                .filter(seat -> seat.getSeatId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Seat> findByShowtimeId(String showtimeId) {
        return findAll().stream()
                .filter(seat -> showtimeId.equals(seat.getShowtimeId()))
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
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(seats));
    }
}
