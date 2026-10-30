package view;

import controller.GameController;
import model.PegBoard;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private GameController controller;
    private JButton[][] buttons;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public BoardPanel(GameController controller) {
        this.controller = controller;
    }

    public void buildBoard() {

        removeAll();

        PegBoard board =
                controller.getGame().getBoard();

        int size = board.getDimension();

        setLayout(new GridLayout(size, size));

        buttons = new JButton[size][size];

        for (int row = 0; row < size; row++) {

            for (int col = 0; col < size; col++) {

                JButton button = new JButton();

                button.setFont(
                        new Font("Arial",
                                Font.BOLD,
                                20));

                int value =
                        board.getCell(row, col);

                if (value == PegBoard.INVALID) {

                    button.setEnabled(false);
                    button.setBorderPainted(false);
                    button.setContentAreaFilled(false);

                } else if (value == PegBoard.PEG) {

                    button.setText("●");

                } else {

                    button.setText("○");
                }

                final int r = row;
                final int c = col;

                button.addActionListener(
                        e -> handleClick(r, c));

                buttons[row][col] = button;

                add(button);
            }
        }

        revalidate();
        repaint();
    }

    private void handleClick(int row,
                             int col) {

        PegBoard board =
                controller.getGame().getBoard();

        int value =
                board.getCell(row, col);

        if (selectedRow == -1) {

            if (value == PegBoard.PEG) {

                selectedRow = row;
                selectedCol = col;

                buttons[row][col]
                        .setBackground(Color.YELLOW);
            }

            return;
        }

        boolean success =
                controller.makeMove(
                        selectedRow,
                        selectedCol,
                        row,
                        col);

        clearSelection();

        if (success) {

            refreshBoard();

            if (controller.gameOver()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Game Over\nRemaining Pegs: "
                                + controller.remainingPegs()
                );
            }
        }
    }

    private void clearSelection() {

        if (selectedRow != -1) {

            buttons[selectedRow][selectedCol]
                    .setBackground(null);
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    public void refreshBoard() {

        PegBoard board =
                controller.getGame().getBoard();

        int size = board.getDimension();

        for (int row = 0; row < size; row++) {

            for (int col = 0; col < size; col++) {

                int value =
                        board.getCell(row, col);

                if (value == PegBoard.PEG) {

                    buttons[row][col]
                            .setText("●");

                } else if (value == PegBoard.EMPTY) {

                    buttons[row][col]
                            .setText("○");
                }
            }
        }
    }
}