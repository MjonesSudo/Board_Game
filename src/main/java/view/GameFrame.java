package view;

import controller.GameController;
import model.BoardType;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private GameController controller;

    private BoardPanel boardPanel;

    private JTextField sizeField;

    private JRadioButton englishButton;
    private JRadioButton hexagonButton;
    private JRadioButton diamondButton;

    public GameFrame() {

        controller = new GameController();

        initializeGUI();
    }

    private void initializeGUI() {

        setTitle("Peg Solitaire");

        setSize(900, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createControlPanel(),
                BorderLayout.WEST);

        boardPanel =
                new BoardPanel(controller);

        add(boardPanel,
                BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createControlPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20));

        JLabel boardTypeLabel =
                new JLabel("Board Type");

        panel.add(boardTypeLabel);

        panel.add(Box.createVerticalStrut(10));

        englishButton =
                new JRadioButton(
                        "English",
                        true);

        hexagonButton =
                new JRadioButton(
                        "Hexagon");

        diamondButton =
                new JRadioButton(
                        "Diamond");

        ButtonGroup group =
                new ButtonGroup();

        group.add(englishButton);
        group.add(hexagonButton);
        group.add(diamondButton);

        panel.add(englishButton);
        panel.add(hexagonButton);
        panel.add(diamondButton);

        panel.add(Box.createVerticalStrut(30));

        JLabel sizeLabel =
                new JLabel("Board Size");

        panel.add(sizeLabel);

        panel.add(Box.createVerticalStrut(5));

        sizeField =
                new JTextField("7");

        sizeField.setMaximumSize(
                new Dimension(
                        100,
                        30));

        panel.add(sizeField);

        panel.add(Box.createVerticalStrut(30));

        JButton newGameButton =
                new JButton("New Game");

        newGameButton.addActionListener(
                e -> startNewGame());

        panel.add(newGameButton);

        return panel;
    }

    private void startNewGame() {

        try {

            int size =
                    Integer.parseInt(
                            sizeField.getText());

            BoardType type =
                    getSelectedBoardType();

            controller.startNewGame(
                    size,
                    type);

            boardPanel.buildBoard();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid board size."
            );
        }
    }

    private BoardType getSelectedBoardType() {

        if (hexagonButton.isSelected()) {
            return BoardType.HEXAGON;
        }

        if (diamondButton.isSelected()) {
            return BoardType.DIAMOND;
        }

        return BoardType.ENGLISH;
    }
}