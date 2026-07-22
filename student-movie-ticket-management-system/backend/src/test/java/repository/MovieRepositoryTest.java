package repository;

import model.Movie;
import model.MovieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {

    @TempDir
    Path tempDir;

    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() throws IOException {
        Path dataFile = tempDir.resolve("movies.json");
        String seedJson = "[" +
                "{\"movieId\":\"MOV001\",\"title\":\"Avengers: Endgame\",\"genre\":\"Hành động\",\"duration\":181,\"ageLimit\":13,\"status\":\"SHOWING\"}," +
                "{\"movieId\":\"MOV002\",\"title\":\"Spider-Man: No Way Home\",\"genre\":\"Hành động\",\"duration\":148,\"ageLimit\":13,\"status\":\"SHOWING\"}," +
                "{\"movieId\":\"MOV003\",\"title\":\"Cô gái đến từ hôm qua\",\"genre\":\"Tình cảm\",\"duration\":110,\"ageLimit\":13,\"status\":\"STOPPED\"}" +
                "]";
        Files.writeString(dataFile, seedJson);
        movieRepository = new MovieRepository(dataFile.toString());
    }

    @Test
    void findAll_returnsEveryStoredMovie() {
        List<Movie> movies = movieRepository.findAll();
        assertEquals(3, movies.size());
    }

    @Test
    void findById_existingMovie_returnsMovie() {
        Movie movie = movieRepository.findById("MOV001");
        assertNotNull(movie);
        assertEquals("Avengers: Endgame", movie.getTitle());
    }

    @Test
    void findById_nonExistentMovie_returnsNull() {
        assertNull(movieRepository.findById("MOV999"));
    }

    @Test
    void findByStatus_showing_excludesStoppedMovies() {
        List<Movie> showing = movieRepository.findByStatus(MovieStatus.SHOWING);
        assertEquals(2, showing.size());
        assertTrue(showing.stream().allMatch(m -> m.getStatus() == MovieStatus.SHOWING));
    }

    @Test
    void searchByTitle_isCaseInsensitiveAndMatchesPartialTitle() {
        List<Movie> results = movieRepository.searchByTitle("spider-man");
        assertEquals(1, results.size());
        assertEquals("MOV002", results.get(0).getMovieId());
    }

    @Test
    void searchByTitle_noMatch_returnsEmptyList() {
        assertTrue(movieRepository.searchByTitle("khong-ton-tai").isEmpty());
    }
}
