package service;

import model.Seat;
import model.SeatStatus;
import repository.SeatRepository;
import exception.SeatNotFoundException;
import exception.SeatAlreadyBookedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    public Seat findById(String seatId) {
        return seatRepository.findById(seatId);
    }

    public List<Seat> findByShowtimeId(String showtimeId) {
        return seatRepository.findByShowtimeId(showtimeId);
    }

    public List<Seat> getAvailableSeats(String showtimeId) {
        return seatRepository.findAvailableSeats(showtimeId);
    }

    public boolean isAvailable(String seatId) {
        Seat seat = seatRepository.findById(seatId);
        return seat != null && seat.getStatus() == SeatStatus.AVAILABLE;
    }

    /**
     * Đặt ghế: kiểm tra ghế tồn tại, còn trống rồi cập nhật trạng thái -> BOOKED.
     */
    public void bookSeat(String seatId) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) {
            throw new SeatNotFoundException("Không tìm thấy ghế với ID: " + seatId);
        }
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatAlreadyBookedException("Ghế " + seatId + " đã được đặt, không thể chọn");
        }
        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.update(seat);
    }
}
