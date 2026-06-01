import javax.swing.JFrame;

public class App {

    public static void main(String[] args) {

        final int boardWidth = 360;
        final int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");

        GamePanel gamePanel = new GamePanel(boardWidth, boardHeight);

        frame.add(gamePanel);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gamePanel.requestFocusInWindow();
    }
}