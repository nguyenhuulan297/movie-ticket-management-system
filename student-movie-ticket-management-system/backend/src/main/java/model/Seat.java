package model;

public class Seat {
    private String seatId;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private String showtimeId;

    public Seat() {
    }

    public Seat(String seatId, String seatNumber, SeatType seatType, SeatStatus status, String showtimeId) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = status;
        this.showtimeId = showtimeId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId='" + seatId + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", seatType=" + seatType +
                ", status=" + status +
                ", showtimeId='" + showtimeId + '\'' +
                '}';
    }
}
