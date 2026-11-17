package view;

import controller.GameController;
import model.PegBoard;
import javax.swing.*;
import java.awt.*;

// *STILL WORKING ON COMMENTS AFTER GETTING HEXAGON AND DIAMOND BOARDS TO FINALLY WORK*

public class BoardPanel extends JPanel {
    private final GameController controller;
    private JButton[][] buttons;
    private int selectedRow = -1, selectedCol = -1;
    private boolean interactive = true;
    private Runnable onChange = () -> {};

    public BoardPanel(GameController controller) { this.controller = controller; }
    public void setOnChange(Runnable listener) { onChange = listener; }
    public void setInteractive(boolean enabled) { interactive = enabled; }
    public void buildBoard() {
        removeAll();
        selectedRow = selectedCol = -1;
        PegBoard board = controller.getGame().getBoard();
        int n = board.getDimension();
        setLayout(null);
        int cell = Math.min(52, Math.max(25, 560 / n));
        int step = cell + 2;
        int centerX = 360;
        int top = 12;
        setPreferredSize(new Dimension(720, n * step + 24));
        buttons = new JButton[n][n];
        for (int r=0; r<n; r++) for (int c=0; c<n; c++) {
            final int row=r, col=c;
            JButton b = new JButton();
            b.setFont(new Font("SansSerif",Font.BOLD, Math.max(12, 24-n/2)));
            b.setMargin(new Insets(0,0,0,0));
            b.setFocusPainted(false);
            if (board.getCell(r,c)==PegBoard.INVALID) {
                b.setVisible(false);
            } else {
                b.addActionListener(e -> handleClick(row,col));
            }
            buttons[r][c]=b;
            double x = board.getBoardType() == model.BoardType.HEXAGON
                    ? (c + r / 2.0 - (n - 1) * 0.75)
                    : c - (n - 1) / 2.0;
            b.setBounds((int)Math.round(centerX + x * step - cell / 2.0),
                    top + r * step, cell, cell);
            add(b);
        }
        refreshBoard();
        revalidate(); repaint();
    }
    private void handleClick(int r, int c) {
        if (!interactive) return;
        PegBoard board = controller.getGame().getBoard();
        if (selectedRow == -1) {
            if (board.getCell(r,c)==PegBoard.PEG) { selectedRow=r; selectedCol=c; refreshBoard(); }
            return;
        }
        if (r==selectedRow && c==selectedCol) { clearSelection(); return; }
        if (controller.makeMove(selectedRow,selectedCol,r,c)) {
            clearSelection(); onChange.run();
            if (controller.gameOver()) JOptionPane.showMessageDialog(this,
                "Game Over!\nRemaining pegs: " + controller.remainingPegs() + "\nRating: " + controller.rating());
        } else if (board.getCell(r,c)==PegBoard.PEG) {
            selectedRow=r; selectedCol=c; refreshBoard();
        } else clearSelection();
    }
    public void clearSelection() { selectedRow=selectedCol=-1; refreshBoard(); }
    public void refreshBoard() {
        if (buttons==null || controller.getGame()==null) return;
        PegBoard board = controller.getGame().getBoard();
        for (int r=0;r<board.getDimension();r++) for (int c=0;c<board.getDimension();c++) {
            JButton b=buttons[r][c];
            int value=board.getCell(r,c);
            b.setText(value==PegBoard.PEG ? "●" : value==PegBoard.EMPTY ? "○" : "");
            b.setBackground(r==selectedRow && c==selectedCol ? Color.YELLOW : null);
            b.setOpaque(r==selectedRow && c==selectedCol);
        }
        repaint();
    }
}
