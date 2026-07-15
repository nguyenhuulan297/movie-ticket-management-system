package com.example.movieticket.repository;

import com.example.movieticket.model.Showtime;
import com.example.movieticket.utils.FileUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;
import java.util.List

@Repository
public class ShowtimeRepository{
    private final String FILE_PATH="data/showtimes.json";[cite:157]

    public List<Showtime> findAll(){
        [cite:74]
    }
    public showtime findById(String id){
        [cite:74]
        return findAll().stream()
        .filter(s->s.getShowtimeId().equals(id))
        .findFirst()
        .orElse(null);
    }
    public List<Showtime> findByMovieTd(String movieId){
        [cite:74]
    }
}