package com.example.movieticket.handler;

import com.example.movieticket.model.Showtime;
import com.example.movieticket.service.ShowtimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeHandler{
    private final ShowtimeService showtimeService;

    public ShowtimeHandler(ShowtimeService showtimeService){
        this.showtimeService=showtimeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getById(@PathVariable String id){  
        return ResponseEntity.ok(showtimeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Showtime>> getByMovieId(@RequestParam String movieId){
    }
}