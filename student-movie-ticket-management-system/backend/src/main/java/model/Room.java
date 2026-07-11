package model;

public class Room {
    private String roomId;
    private String roomName;
    private int totalSeats;

    public Room() {
    }

    public Room(String roomId, String roomName, int totalSeats) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.totalSeats = totalSeats;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
