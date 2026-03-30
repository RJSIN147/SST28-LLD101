public class Seat {
    private final int seatNumber;
    private final SeatType type;
    private final int row;

    public Seat(int seatNumber, SeatType type, int row) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.row = row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public SeatType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "Seat{#" + seatNumber + ", row=" + row + ", type=" + type + "}";
    }
}
