package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Showtime;
import utils.FileUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeRepository {
    private static final String FILE_PATH = "data/showtimes.json";
    private final Gson gson = new Gson();

    public List<Showtime> findAll() {
        String json = FileUtils.readJsonFile(FILE_PATH);
        Type listType = new TypeToken<List<Showtime>>() {}.getType();
        List<Showtime> showtimes = gson.fromJson(json, listType);
        return showtimes != null ? showtimes : new ArrayList<>();
    }

    public Showtime findById(String id) {
        return findAll().stream()
                .filter(s -> s.getShowtimeId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
