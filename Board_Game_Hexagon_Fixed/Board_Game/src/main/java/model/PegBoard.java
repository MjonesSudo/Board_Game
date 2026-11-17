package model;

import java.util.Arrays;

public class PegBoard {
    public static final int INVALID = -1, EMPTY = 0, PEG = 1;
    private final int[][] board;
    private final BoardType boardType;
    private final int boardSize;

    // Initializes board using board size and type
    public PegBoard(int size, BoardType type) {
        if (size < 3 || size > 15 || size % 2 == 0)
            throw new IllegalArgumentException("Board size must be odd, from 3 to 15.");
        if (type == null) throw new IllegalArgumentException("Choose a board type.");
        boardSize = size;
        boardType = type;
        int dimension = size;
        board = new int[dimension][dimension];
        for (int[] row : board) Arrays.fill(row, INVALID);
        switch (type) {
            case ENGLISH -> createEnglishBoard();
            case HEXAGON -> createHexagonBoard();
            case DIAMOND -> createDiamondBoard();
        }
        board[dimension / 2][dimension / 2] = EMPTY;
    }

    // Creates English board
    private void createEnglishBoard() {
        int arm = boardSize / 3;
        for (int r = 0; r < boardSize; r++)
            for (int c = 0; c < boardSize; c++)
                if ((r >= arm && r < boardSize - arm) ||
                    (c >= arm && c < boardSize - arm)) board[r][c] = PEG;
    }

    // Created Hexagon board
    private void createHexagonBoard() {
        int n = (boardSize + 1) / 2, dimension = board.length;
        for (int r = 0; r < dimension; r++) {
            int start = Math.max(0, n - 1 - r);
            int end = Math.min(dimension - 1, 3 * n - 3 - r);
            for (int c = start; c <= end; c++) board[r][c] = PEG;
        }
    }

    // Creates Diamond board
    private void createDiamondBoard() {
        int center = board.length / 2;
        for (int r = 0; r < board.length; r++) {
            int radius = center - Math.abs(r - center);
            for (int c = center - radius; c <= center + radius; c++) board[r][c] = PEG;
        }
    }

    // Returns the current cell
    public int getCell(int row, int col) { return board[row][col]; }

    // Updates cell
    public void setCell(int row, int col, int value) {
        if (board[row][col] == INVALID || value == INVALID)
            throw new IllegalArgumentException("Cannot change invalid board cell.");
        if (value != PEG && value != EMPTY) throw new IllegalArgumentException("Invalid cell value.");
        board[row][col] = value;
    }

    // Returns board length
    public int getDimension() { return board.length; }

    // Returns board type
    public BoardType getBoardType() { return boardType; }

    // Returns board size
    public int getBoardSize() { return boardSize; }

    // Verifying if the coordinates are within bounds of the board
    public boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < board.length && col < board.length;
    }

    // Returns a copy of the board's cell values
    public int[][] getBoard() {
        int[][] copy = new int[board.length][];
        for (int r = 0; r < board.length; r++) copy[r] = board[r].clone();
        return copy;
    }
}
