package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// *STILL WORKING ON COMMENTS AFTER GETTING HEXAGON AND DIAMOND BOARDS TO FINALLY WORK*
public class PegSolitaireGame {
    private final PegBoard board;
    private final Random random = new Random();
    private static final int[][] ALL_DIRECTIONS = {
        {2,0}, {-2,0}, {0,2}, {0,-2},
        {2,2}, {-2,-2}, {2,-2}, {-2,2}
    };
    // A hexagon in our skewed array has three axes: row, column, row+column.
    private static final int[][] HEX_DIRECTIONS = {
        {2,0}, {-2,0}, {0,2}, {0,-2}, {2,-2}, {-2,2}
    };

    public PegSolitaireGame(int size, BoardType type) { board = new PegBoard(size, type); }
    public PegBoard getBoard() { return board; }
    private int[][] directions() {
        return board.getBoardType() == BoardType.HEXAGON ? HEX_DIRECTIONS : ALL_DIRECTIONS;
    }
    public boolean isValidMove(int fr, int fc, int tr, int tc) {
        if (!board.inBounds(fr, fc) || !board.inBounds(tr, tc)) return false;
        if (board.getCell(fr, fc) != PegBoard.PEG || board.getCell(tr, tc) != PegBoard.EMPTY) return false;
        int dr = tr - fr, dc = tc - fc;
        boolean directionFound = false;
        for (int[] d : directions()) if (d[0] == dr && d[1] == dc) { directionFound = true; break; }
        if (!directionFound) return false;
        return board.getCell(fr + dr / 2, fc + dc / 2) == PegBoard.PEG;
    }
    public boolean move(int fr, int fc, int tr, int tc) {
        if (!isValidMove(fr, fc, tr, tc)) return false;
        board.setCell(fr, fc, PegBoard.EMPTY);
        board.setCell((fr + tr) / 2, (fc + tc) / 2, PegBoard.EMPTY);
        board.setCell(tr, tc, PegBoard.PEG);
        return true;
    }
    public List<Move> getLegalMoves() {
        List<Move> result = new ArrayList<>();
        for (int r = 0; r < board.getDimension(); r++)
            for (int c = 0; c < board.getDimension(); c++)
                if (board.getCell(r,c) == PegBoard.PEG)
                    for (int[] d : directions())
                        if (isValidMove(r,c,r+d[0],c+d[1]))
                            result.add(new Move(new Position(r,c), new Position(r+d[0],c+d[1])));
        return result;
    }
    public Move playRandomMove() {
        List<Move> moves = getLegalMoves();
        if (moves.isEmpty()) return null;
        Move move = moves.get(random.nextInt(moves.size()));
        move(move.getFrom().getRow(), move.getFrom().getCol(), move.getTo().getRow(), move.getTo().getCol());
        return move;
    }
    public boolean gameOver() { return getLegalMoves().isEmpty(); }
    public int getPegCount() {
        int count = 0;
        for (int[] row : board.getBoard()) for (int cell : row) if (cell == PegBoard.PEG) count++;
        return count;
    }
    public String getRating() {
        int count = getPegCount();
        return count == 1 ? "Outstanding" : count == 2 ? "Very Good" : count == 3 ? "Good" : "Average";
    }
}
