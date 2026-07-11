package service;

import model.Seat;
import model.SeatStatus;
import org.springframework.stereotype.Service;
import repository.SeatRepository;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService() {
        this.seatRepository = new SeatRepository();
    }

    public Seat findById(String seatId) {
        return seatRepository.findById(seatId);
    }

    public boolean isAvailable(String seatId) {
        Seat seat = findById(seatId);
        return seat != null && seat.getStatus() == SeatStatus.AVAILABLE;
    }

    public void bookSeat(String seatId) {
        Seat seat = findById(seatId);
        if (seat == null) {
            throw new IllegalArgumentException("Ghế không tồn tại: " + seatId);
        }
        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.update(seat);
    }
}
