package service;

import model.Movie;
import model.MovieStatus;
import model.Showtime;
import org.springframework.stereotype.Service;
import repository.MovieRepository;
import repository.ShowtimeRepository;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService() {
        this.showtimeRepository = new ShowtimeRepository();
        this.movieRepository = new MovieRepository();
    }

    public Showtime findById(String id) {
        return showtimeRepository.findById(id);
    }

    public boolean isMovieShowing(Showtime showtime) {
        if (showtime == null) {
            return false;
        }
        Movie movie = movieRepository.findById(showtime.getMovieId());
        return movie != null && movie.getStatus() == MovieStatus.SHOWING;
    }
}
