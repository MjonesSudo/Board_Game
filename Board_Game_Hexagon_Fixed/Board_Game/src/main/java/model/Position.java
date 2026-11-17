package model;

public class Position {

    private int row;
    private int col;

    // Represents a position using the columns and rows
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Returns row coordinates
    public int getRow() {
        return row;
    }

    // Return column coordinates
    public int getCol() {
        return col;
    }
}