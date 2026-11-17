package controller;

import model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// *STILL WORKING ON COMMENTS AFTER GETTING HEXAGON AND DIAMOND BOARDS TO FINALLY WORK*

public class GameController {
    private PegSolitaireGame game;
    private int size;
    private BoardType type;
    private final List<String> recordedActions = new ArrayList<>();
    private boolean recording;
    private boolean replaying;

    public void startNewGame(int size, BoardType type) {
        game = new PegSolitaireGame(size, type);
        this.size = size;
        this.type = type;
        recordedActions.clear();
    }
    public PegSolitaireGame getGame() { return game; }
    public boolean makeMove(int fr, int fc, int tr, int tc) {
        boolean success = game.move(fr,fc,tr,tc);
        if (success && recording && !replaying) recordedActions.add("MOVE " + fr + " " + fc + " " + tr + " " + tc);
        return success;
    }
    public Move autoplayStep() {
        Move move = game.playRandomMove();
        if (move != null && recording && !replaying) recordedActions.add("MOVE " + move.getFrom().getRow() + " " + move.getFrom().getCol() + " " + move.getTo().getRow() + " " + move.getTo().getCol());
        return move;
    }
    public int randomize(int maxMoves) {
        int count = 0;
        for (int i = 0; i < maxMoves; i++) {
            if (autoplayStep() == null) break;
            count++;
        }
        return count;
    }
    public void setRecording(boolean enabled) { recording = enabled; }
    public boolean isRecording() { return recording; }
    public boolean gameOver() { return game.gameOver(); }
    public int remainingPegs() { return game.getPegCount(); }
    public String rating() { return game.getRating(); }

    public void saveRecording(Path file) throws IOException {
        if (game == null) throw new IllegalStateException("Start a game first.");
        List<String> lines = new ArrayList<>();
        lines.add("PEGA1 " + size + " " + type.name());
        lines.addAll(recordedActions);
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
    public List<Move> loadRecording(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.isEmpty()) throw new IOException("Empty recording.");
        String[] header = lines.get(0).trim().split("\s+");
        if (header.length != 3 || !header[0].equals("PEGA1")) throw new IOException("Invalid recording header.");
        int loadedSize;
        BoardType loadedType;
        try { loadedSize = Integer.parseInt(header[1]); loadedType = BoardType.valueOf(header[2]); }
        catch (IllegalArgumentException e) { throw new IOException("Invalid board settings.", e); }
        PegSolitaireGame validation;
        try { validation = new PegSolitaireGame(loadedSize, loadedType); }
        catch (IllegalArgumentException e) { throw new IOException("Invalid board settings.", e); }
        List<Move> moves = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] tokens = lines.get(i).trim().split("\s+");
            if (tokens.length != 5 || !tokens[0].equals("MOVE")) throw new IOException("Invalid move on line " + (i+1));
            try {
                int fr = Integer.parseInt(tokens[1]), fc = Integer.parseInt(tokens[2]);
                int tr = Integer.parseInt(tokens[3]), tc = Integer.parseInt(tokens[4]);
                if (!validation.move(fr,fc,tr,tc)) throw new IOException("Illegal move on line " + (i+1));
                moves.add(new Move(new Position(fr,fc), new Position(tr,tc)));
            } catch (NumberFormatException e) { throw new IOException("Invalid coordinates on line " + (i+1), e); }
        }
        replaying = true;
        try { startNewGame(loadedSize, loadedType); }
        finally { replaying = false; }
        return moves;
    }
}
