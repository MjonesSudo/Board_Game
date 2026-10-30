package controller;

import model.BoardType;
import model.PegSolitaireGame;

public class GameController {

    private PegSolitaireGame game;

    public void startNewGame(int size,
                             BoardType type) {

        game =
                new PegSolitaireGame(
                        size,
                        type);
    }

    public PegSolitaireGame getGame() {
        return game;
    }

    public boolean makeMove(int fromRow,
                            int fromCol,
                            int toRow,
                            int toCol) {

        return game.move(
                fromRow,
                fromCol,
                toRow,
                toCol);
    }

    public boolean gameOver() {
        return game.gameOver();
    }

    public int remainingPegs() {
        return game.getPegCount();
    }
}