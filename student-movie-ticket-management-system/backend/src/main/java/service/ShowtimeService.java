package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import model.Seat;
import model.Showtime;
import repository.ShowtimeRepository;
import utils.FileUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {
    private static final String DEFAULT_MOVIES_FILE_PATH = "data/movies.json";
    private static final String MOVIE_SHOWING_STATUS = "SHOWING";

    private final ShowtimeRepository showtimeRepository;
    private final SeatService seatService;
    private final String moviesFilePath;

    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatService seatService) {
        this(showtimeRepository, seatService, DEFAULT_MOVIES_FILE_PATH);
    }

    /**
     * Cho phép truyền moviesFilePath tùy chỉnh (dùng khi test).
     * Đọc thẳng movies.json vì module Movie (Thành viên 2) chưa cung cấp MovieService.
     */
    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatService seatService, String moviesFilePath) {
        this.showtimeRepository = showtimeRepository;
        this.seatService = seatService;
        this.moviesFilePath = moviesFilePath;
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
        JsonArray movies = JsonParser.parseString(FileUtils.readJsonFile(moviesFilePath)).getAsJsonArray();
        for (int i = 0; i < movies.size(); i++) {
            var movie = movies.get(i).getAsJsonObject();
            if (showtime.getMovieId().equals(movie.get("movieId").getAsString())) {
                return MOVIE_SHOWING_STATUS.equals(movie.get("status").getAsString());
            }
        }
        return false;
    }

    public List<Seat> getAvailableSeats(String showtimeId) {
        return seatService.getAvailableSeats(showtimeId);
    }

    public boolean hasAvailableSeats(String showtimeId) {
        return !getAvailableSeats(showtimeId).isEmpty();
    }
}
