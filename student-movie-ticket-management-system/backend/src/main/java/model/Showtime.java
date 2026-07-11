package model;

public class Showtime {
    private String showtimeId;
    private String movieId;
    private Room room;
    private String startTime;
    private String endTime;
    private double basePrice;

    public Showtime() {
    }

    public Showtime(String showtimeId, String movieId, Room room, String startTime, String endTime, double basePrice) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.basePrice = basePrice;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "showtimeId='" + showtimeId + '\'' +
                ", movieId='" + movieId + '\'' +
                ", room=" + room +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}
