package model;

public class PegSolitaireGame {

    private PegBoard board;

    public PegSolitaireGame(int size,
                            BoardType type) {

        board = new PegBoard(size, type);
    }

    public PegBoard getBoard() {
        return board;
    }

    public boolean move(int fromRow,
                        int fromCol,
                        int toRow,
                        int toCol) {

        if (!isValidMove(
                fromRow,
                fromCol,
                toRow,
                toCol)) {

            return false;
        }

        int middleRow =
                (fromRow + toRow) / 2;

        int middleCol =
                (fromCol + toCol) / 2;

        board.setCell(fromRow, fromCol,
                PegBoard.EMPTY);

        board.setCell(middleRow, middleCol,
                PegBoard.EMPTY);

        board.setCell(toRow, toCol,
                PegBoard.PEG);

        return true;
    }

    public boolean isValidMove(int fromRow,
                               int fromCol,
                               int toRow,
                               int toCol) {

        try {

            if (board.getCell(fromRow, fromCol)
                    != PegBoard.PEG)
                return false;

            if (board.getCell(toRow, toCol)
                    != PegBoard.EMPTY)
                return false;

            int rowDiff =
                    Math.abs(toRow - fromRow);

            int colDiff =
                    Math.abs(toCol - fromCol);

            if (!((rowDiff == 2 && colDiff == 0)
                    ||
                    (rowDiff == 0 && colDiff == 2)))
                return false;

            int middleRow =
                    (fromRow + toRow) / 2;

            int middleCol =
                    (fromCol + toCol) / 2;

            return board.getCell(
                    middleRow,
                    middleCol)
                    == PegBoard.PEG;

        } catch (Exception ex) {
            return false;
        }
    }

    public boolean gameOver() {

        int size = board.getDimension();

        for (int r = 0; r < size; r++) {

            for (int c = 0; c < size; c++) {

                if (board.getCell(r, c)
                        == PegBoard.PEG) {

                    if (hasMove(r, c))
                        return false;
                }
            }
        }

        return true;
    }

    private boolean hasMove(int row,
                            int col) {

        int[][] moves = {
                {2, 0},
                {-2, 0},
                {0, 2},
                {0, -2}
        };

        for (int[] move : moves) {

            int newRow =
                    row + move[0];

            int newCol =
                    col + move[1];

            if (isValidMove(
                    row,
                    col,
                    newRow,
                    newCol)) {

                return true;
            }
        }

        return false;
    }

    public int getPegCount() {

        int count = 0;

        for (int r = 0;
             r < board.getDimension();
             r++) {

            for (int c = 0;
                 c < board.getDimension();
                 c++) {

                if (board.getCell(r, c)
                        == PegBoard.PEG) {

                    count++;
                }
            }
        }

        return count;
    }
}