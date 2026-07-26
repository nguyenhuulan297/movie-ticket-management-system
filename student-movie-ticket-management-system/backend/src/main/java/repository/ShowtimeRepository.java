package repository;

import model.Showtime;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShowtimeRepository {
    private static final String DEFAULT_FILE_PATH = "data/showtimes.json";
    private final String filePath;
    private final Gson gson = new Gson();

    public ShowtimeRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/showtimes.json).
     */
    public ShowtimeRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Showtime> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Showtime>>(){}.getType();
        List<Showtime> showtimes = gson.fromJson(json, listType);
        return showtimes != null ? showtimes : new ArrayList<>();
    }

    public Showtime findById(String showtimeId) {
        return findAll().stream()
                .filter(s -> s.getShowtimeId().equals(showtimeId))
                .findFirst()
                .orElse(null);
    }

    public List<Showtime> findByMovieId(String movieId) {
        return findAll().stream()
                .filter(s -> s.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}