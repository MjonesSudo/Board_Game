package model;

public class Move {

    private Position from;
    private Position to;

    // Creates new move using previous move coordinates
    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    // Returns the starting position
    public Position getFrom() {
        return from;
    }

    // Returns the destination position
    public Position getTo() {
        return to;
    }
}