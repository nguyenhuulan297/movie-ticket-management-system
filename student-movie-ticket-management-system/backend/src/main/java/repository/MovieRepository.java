package repository;

import model.Movie;
import model.MovieStatus;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovieRepository {
    private static final String DEFAULT_FILE_PATH = "data/movies.json";

    private final String filePath;
    private final Gson gson = new Gson();

    public MovieRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/movies.json).
     */
    public MovieRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Movie> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> movies = gson.fromJson(json, listType);
        return movies != null ? movies : new ArrayList<>();
    }

    public Movie findById(String movieId) {
        return findAll().stream()
                .filter(m -> m.getMovieId().equals(movieId))
                .findFirst()
                .orElse(null);
    }

    public List<Movie> findByStatus(MovieStatus status) {
        return findAll().stream()
                .filter(m -> m.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Movie> searchByTitle(String title) {
        String keyword = title == null ? "" : title.toLowerCase();
        return findAll().stream()
                .filter(m -> m.getTitle() != null && m.getTitle().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }
}
