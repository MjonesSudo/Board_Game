package main;

import javax.swing.SwingUtilities;
import view.GameFrame;

public class Main {

    public static void main(String[] args) {
        // Launches GUI for game
        SwingUtilities.invokeLater(GameFrame::new);
    }
}