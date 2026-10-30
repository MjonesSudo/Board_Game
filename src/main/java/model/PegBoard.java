package model;

public class PegBoard {

    public static final int INVALID = -1;
    public static final int EMPTY = 0;
    public static final int PEG = 1;

    private int[][] board;
    private BoardType boardType;
    private int boardSize;

    public PegBoard(int boardSize, BoardType boardType) {
        this.boardSize = boardSize;
        this.boardType = boardType;

        switch (boardType) {
            case ENGLISH:
                createEnglishBoard();
                break;

            case HEXAGON:
                createHexagonBoard();
                break;

            case DIAMOND:
                createDiamondBoard();
                break;
        }
    }

    private void createEnglishBoard() {

        int dimension = boardSize;
        board = new int[dimension][dimension];

        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {
                board[r][c] = INVALID;
            }
        }

        int arm = dimension / 3;

        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {

                boolean horizontal =
                        (r >= arm && r < dimension - arm);

                boolean vertical =
                        (c >= arm && c < dimension - arm);

                if (horizontal || vertical) {
                    board[r][c] = PEG;
                }
            }
        }

        board[dimension / 2][dimension / 2] = EMPTY;
    }

    private void createHexagonBoard() {
        int dimension = 2 * boardSize - 1;
        board = new int[dimension][dimension];

        // Initialize all cells as invalid
        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {
                board[r][c] = INVALID;
            }
        }

        // Create the hexagon
        for (int r = 0; r < dimension; r++) {

            int startCol;
            int cellsInRow;

            if (r < boardSize) {
                startCol = boardSize - 1 - r;
                cellsInRow = boardSize + r;
            } else {
                startCol = r - boardSize + 1;
                cellsInRow = dimension - startCol;
            }

            for (int c = startCol; c < startCol + cellsInRow; c++) {
                board[r][c] = PEG;
            }
        }

        // Empty center hole
        board[dimension / 2][dimension / 2] = EMPTY;
    }
    private void createDiamondBoard() {

        board = new int[boardSize][boardSize];

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = PEG;
            }
        }

        board[boardSize / 2][boardSize / 2] = EMPTY;
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    public int getDimension() {
        return board.length;
    }

    public int[][] getBoard() {
        return board;
    }
}