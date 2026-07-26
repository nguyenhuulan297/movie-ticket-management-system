package service;

import model.Movie;
import model.MovieStatus;
import repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(String movieId) {
        return movieRepository.findById(movieId);
    }

    public List<Movie> getShowingMovies() {
        return movieRepository.findByStatus(MovieStatus.SHOWING);
    }

    public List<Movie> searchByTitle(String title) {
        return movieRepository.searchByTitle(title);
    }

    public boolean isShowing(String movieId) {
        Movie movie = movieRepository.findById(movieId);
        return movie != null && movie.getStatus() == MovieStatus.SHOWING;
    }
}
