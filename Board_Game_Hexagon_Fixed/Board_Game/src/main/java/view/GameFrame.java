package view;

import controller.GameController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

// *STILL WORKING ON COMMENTS AFTER GETTING HEXAGON AND DIAMOND BOARDS TO FINALLY WORK*

public class GameFrame extends JFrame {
    private final GameController controller = new GameController();
    private BoardPanel boardPanel;
    private JTextField sizeField;
    private JRadioButton englishButton, hexagonButton, diamondButton;
    private JCheckBox recordBox;
    private JLabel status;
    private Timer timer;

    public GameFrame() {
        setTitle("Peg Solitaire — CS449");
        setSize(1050,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        add(createControlPanel(),BorderLayout.WEST);
        boardPanel=new BoardPanel(controller);
        boardPanel.setOnChange(this::updateStatus);
        add(boardPanel,BorderLayout.CENTER);
        status=new JLabel(" ");
        add(status,BorderLayout.SOUTH);
        startNewGame();
        setVisible(true);
    }
    private JPanel createControlPanel() {
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20,15,20,15));
        panel.add(new JLabel("Board Type"));
        englishButton=new JRadioButton("English",true);
        hexagonButton=new JRadioButton("Hexagon");
        diamondButton=new JRadioButton("Diamond");
        ButtonGroup group=new ButtonGroup();
        for (JRadioButton b : new JRadioButton[]{englishButton,hexagonButton,diamondButton}) {
            group.add(b); panel.add(b);
        }
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JLabel("Board Size (odd: 3–15)"));
        sizeField=new JTextField("7");
        sizeField.setMaximumSize(new Dimension(130,30));
        panel.add(sizeField);
        panel.add(Box.createVerticalStrut(15));
        JButton newGame=new JButton("New Game");
        newGame.addActionListener(e -> startNewGame());
        panel.add(newGame);
        panel.add(Box.createVerticalStrut(15));
        recordBox=new JCheckBox("Record game");
        recordBox.addActionListener(e -> controller.setRecording(recordBox.isSelected()));
        panel.add(recordBox);
        JButton save=new JButton("Save Recording");
        save.addActionListener(e -> saveRecording());
        panel.add(save);
        JButton replay=new JButton("Replay File");
        replay.addActionListener(e -> replayFile());
        panel.add(replay);
        panel.add(Box.createVerticalStrut(15));
        JButton auto=new JButton("Autoplay");
        auto.addActionListener(e -> autoplay());
        panel.add(auto);
        JButton randomize=new JButton("Randomize (3 moves)");
        randomize.addActionListener(e -> randomize());
        panel.add(randomize);
        JButton stop=new JButton("Stop Animation");
        stop.addActionListener(e -> stopTimer());
        panel.add(stop);
        return panel;
    }
    private BoardType selectedType() {
        return hexagonButton.isSelected() ? BoardType.HEXAGON :
            diamondButton.isSelected() ? BoardType.DIAMOND : BoardType.ENGLISH;
    }
    private void stopTimer() {
        if (timer!=null) { timer.stop(); timer=null; }
        if (boardPanel!=null) boardPanel.setInteractive(true);
    }
    private void startNewGame() {
        stopTimer();
        try {
            int size=Integer.parseInt(sizeField.getText().trim());
            controller.startNewGame(size,selectedType());
            controller.setRecording(recordBox.isSelected());
            boardPanel.buildBoard(); updateStatus();
        } catch (IllegalArgumentException e) { showError(e.getMessage()); }
    }
    private void updateStatus() {
        status.setText("  Pegs remaining: " + controller.remainingPegs() +
            " | Legal moves: " + controller.getGame().getLegalMoves().size() +
            (controller.gameOver() ? " | Game over — " + controller.rating() : ""));
        boardPanel.refreshBoard();
    }
    private void autoplay() {
        stopTimer();
        boardPanel.clearSelection(); boardPanel.setInteractive(false);
        timer=new Timer(220,e -> {
            if (controller.autoplayStep()==null) {
                stopTimer(); updateStatus();
                JOptionPane.showMessageDialog(this,"Autoplay finished.\nRemaining pegs: " +
                    controller.remainingPegs() + "\nRating: " + controller.rating());
            } else updateStatus();
        });
        timer.start();
    }
    private void randomize() {
        stopTimer(); boardPanel.clearSelection();
        int moves=controller.randomize(3);
        updateStatus();
        if (moves==0) JOptionPane.showMessageDialog(this,"No legal moves remain.");
    }
    private void saveRecording() {
        stopTimer();
        JFileChooser chooser=new JFileChooser();
        chooser.setSelectedFile(new java.io.File("pega-recording.txt"));
        if (chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try { controller.saveRecording(chooser.getSelectedFile().toPath());
                JOptionPane.showMessageDialog(this,"Recording saved.");
            } catch (IOException | IllegalStateException e) { showError(e.getMessage()); }
        }
    }
    private void replayFile() {
        stopTimer();
        JFileChooser chooser=new JFileChooser();
        if (chooser.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION) return;
        try {
            List<Move> moves=controller.loadRecording(chooser.getSelectedFile().toPath());
            controller.setRecording(false);
            sizeField.setText(String.valueOf(controller.getGame().getBoard().getBoardSize()));
            switch(controller.getGame().getBoard().getBoardType()) {
                case ENGLISH -> englishButton.setSelected(true);
                case HEXAGON -> hexagonButton.setSelected(true);
                case DIAMOND -> diamondButton.setSelected(true);
            }
            boardPanel.buildBoard(); updateStatus();
            boardPanel.setInteractive(false);
            final int[] index={0};
            timer=new Timer(300,e -> {
                if (index[0]>=moves.size()) {
                    stopTimer(); controller.setRecording(recordBox.isSelected()); updateStatus();
                    JOptionPane.showMessageDialog(this,"Replay finished.");
                    return;
                }
                Move m=moves.get(index[0]++);
                controller.makeMove(m.getFrom().getRow(),m.getFrom().getCol(),m.getTo().getRow(),m.getTo().getCol());
                updateStatus();
            });
            timer.start();
        } catch (IOException | IllegalArgumentException e) { showError(e.getMessage()); }
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,message,"Peg Solitaire",JOptionPane.ERROR_MESSAGE);
    }
}
