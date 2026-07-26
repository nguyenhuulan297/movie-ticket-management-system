package service;

import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.MovieRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    @TempDir
    Path tempDir;

    private MovieService movieService;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("movies.json");
        String seedJson = "[" +
                "{\"movieId\":\"MOV001\",\"title\":\"Avengers: Endgame\",\"genre\":\"Hành động\",\"duration\":181,\"ageLimit\":13,\"status\":\"SHOWING\"}," +
                "{\"movieId\":\"MOV002\",\"title\":\"Spider-Man: No Way Home\",\"genre\":\"Hành động\",\"duration\":148,\"ageLimit\":13,\"status\":\"SHOWING\"}," +
                "{\"movieId\":\"MOV003\",\"title\":\"Cô gái đến từ hôm qua\",\"genre\":\"Tình cảm\",\"duration\":110,\"ageLimit\":13,\"status\":\"STOPPED\"}" +
                "]";
        Files.writeString(dataFile, seedJson);
        MovieRepository movieRepository = new MovieRepository(dataFile.toString());
        movieService = new MovieService(movieRepository);
    }

    // ---- Tình huống: hiển thị danh sách phim đang chiếu, không lẫn phim ngừng chiếu ----
    @Test
    void getShowingMovies_excludesStoppedMovies() {
        List<Movie> showing = movieService.getShowingMovies();
        assertEquals(2, showing.size());
        assertTrue(showing.stream().noneMatch(m -> m.getMovieId().equals("MOV003")));
    }

    @Test
    void searchByTitle_findsMatchByPartialName() {
        List<Movie> results = movieService.searchByTitle("Endgame");
        assertEquals(1, results.size());
        assertEquals("MOV001", results.get(0).getMovieId());
    }

    // ---- Tình huống: phim đang chiếu -> isShowing trả về true ----
    @Test
    void isShowing_movieCurrentlyShowing_returnsTrue() {
        assertTrue(movieService.isShowing("MOV001"));
    }

    // ---- Tình huống: phim đã ngừng chiếu -> isShowing trả về false (không cho đặt vé) ----
    @Test
    void isShowing_stoppedMovie_returnsFalse() {
        assertFalse(movieService.isShowing("MOV003"));
    }

    @Test
    void isShowing_nonExistentMovie_returnsFalseInsteadOfThrowing() {
        assertFalse(movieService.isShowing("MOV999"));
    }

    @Test
    void findById_delegatesToRepository() {
        Movie movie = movieService.findById("MOV002");
        assertNotNull(movie);
        assertEquals("Spider-Man: No Way Home", movie.getTitle());
    }
}
