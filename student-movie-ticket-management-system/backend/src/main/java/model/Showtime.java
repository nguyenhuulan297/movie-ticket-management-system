package com.example.movieticket.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data //tu sinh get,set,toS nho lombok
public class Showtime{
    private String ShowtimeId;[cite:72]
    private Movie movie;
    private CinemaRoom room;
    private LocalDateTime startTime;[cite:72]
    private LocalDateTime endTime;[cite:72]
    private double basePrive;
    private List<Seat> seats;
}

