package service;

import model.Movie;
import model.MovieStatus;
import model.Seat;
import model.Showtime;
import org.springframework.stereotype.Service;
import repository.MovieRepository;
import repository.ShowtimeRepository;
import java.util.List;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final SeatService seatService;

    public ShowtimeService(ShowtimeRepository showtimeRepository,
                            MovieRepository movieRepository,
                            SeatService seatService) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.seatService = seatService;
    }

    public List<Showtime> findAll() {
        return showtimeRepository.findAll();
    }

    public Showtime findById(String showtimeId) {
        return showtimeRepository.findById(showtimeId);
    }

    public List<Showtime> findByMovieId(String movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    public boolean exists(String showtimeId) {
        return showtimeRepository.findById(showtimeId) != null;
    }

    public boolean isMovieShowing(Showtime showtime) {
        if (showtime == null || showtime.getMovieId() == null) {
            return false;
        }
        Movie movie = movieRepository.findById(showtime.getMovieId());
        return movie != null && movie.getStatus() == MovieStatus.SHOWING;
    }

    public List<Seat> getAvailableSeats(String showtimeId) {
        return seatService.getAvailableSeats(showtimeId);
    }

    public boolean hasAvailableSeats(String showtimeId) {
        return !getAvailableSeats(showtimeId).isEmpty();
    }
}