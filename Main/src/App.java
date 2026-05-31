import javax.swing.JFrame;

public class App {
    public static void main(String[] args){
        final int boardHeight= 640;
        final int boardWidth= 360;

        JFrame frame = new JFrame("Game Project");
        GamePanel gamePanel = new GamePanel(boardHeight,boardWidth);

        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

        gamePanel.requestFocusInWindow();
    }
}
