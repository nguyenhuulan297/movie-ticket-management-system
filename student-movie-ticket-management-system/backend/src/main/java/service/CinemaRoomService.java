package service;

import model.CinemaRoom;
import repository.CinemaRoomRepository;
import utils.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;

    public CinemaRoomService(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    public List<CinemaRoom> findAll() {
        return cinemaRoomRepository.findAll();
    }

    public CinemaRoom findById(String roomId) {
        return cinemaRoomRepository.findById(roomId);
    }

    public CinemaRoom create(CinemaRoom room) {
        if (room.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Tổng số ghế phải lớn hơn 0");
        }
        room.setRoomId(IdGenerator.generateRoomId());
        cinemaRoomRepository.save(room);
        return room;
    }

    public CinemaRoom update(CinemaRoom room) {
        if (cinemaRoomRepository.findById(room.getRoomId()) == null) {
            return null;
        }
        cinemaRoomRepository.update(room);
        return room;
    }

    public void deleteById(String roomId) {
        cinemaRoomRepository.deleteById(roomId);
    }
}
