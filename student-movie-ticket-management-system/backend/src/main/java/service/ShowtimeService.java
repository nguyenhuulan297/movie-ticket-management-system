package com.example.movieticket.service;

import com.example.movieticket.model.Showtime;
import com.example.movieticket.model.Seat;
import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.exception.ShowtimeNotFoundException;
import org.springframework/stereotype.Service;
import java.util.List

@Service
public class ShowtimeService{
    private final ShowtimeRepository showtimeRepository;
    private final MovieService movieService;
    private final SeatService seatService;

    public ShowtimeService(ShowtimeRepository showtimeRepository,MovieService movieService,SeatService seatService){
        this.showtimeRepository=showtimeRepository;
        this.movieService=movieService;
        this.seatService=seatService;
    }
    public Showtime findById(String id){
        Showtime showtime =showtimeRepository.findById(id);
        if(showtime == null){
            throw new ShowtimeNotFoundException("Không tìm thấy suất chiếu!");[cite:17]
        }
        return showtime;
    }

    public List<Seat> getAvailableSeats(String showtimeId){
        
    }
}