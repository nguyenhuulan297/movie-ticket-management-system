package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Movie;
import utils.FileUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private static final String FILE_PATH = "data/movies.json";
    private final Gson gson = new Gson();

    public List<Movie> findAll() {
        String json = FileUtils.readJsonFile(FILE_PATH);
        Type listType = new TypeToken<List<Movie>>() {}.getType();
        List<Movie> movies = gson.fromJson(json, listType);
        return movies != null ? movies : new ArrayList<>();
    }

    public Movie findById(String id) {
        return findAll().stream()
                .filter(m -> m.getMovieId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
