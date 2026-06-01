import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GamePanel extends JPanel
        implements KeyListener, ActionListener {

    private final int boardWidth;
    private final int boardHeight;

    private final Bird bird;
    private final PipeManager pipeManager;
    private final ScoreManager scoreManager;

    private final int gravity;

    private final Timer gameLoopTimer;
    private final Timer pipeSpawnTimer;

    private GameState gameState;
    private Difficulty difficulty;

    private Image bgImage;
    private Image gameOverImg;

    private final Image[] digits = new Image[10];

    public GamePanel(int boardWidth, int boardHeight) {

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(boardWidth, boardHeight));

        setFocusable(true);
        addKeyListener(this);

        gravity = 1;

        // Images
        bgImage = loadImage("/asset/flappybirdbg.png");
        Image topPipeImage =loadImage("/asset/toppipe.png");
        Image bottomPipeImage =loadImage("/asset/bottompipe.png");
        Image birdImage = loadImage("/asset/bird.png");
        gameOverImg =loadImage("/asset/gameover.png");

        for (int i = 0; i < 10; i++) {
            digits[i] = loadImage("/asset/" + i + ".png");
        }

        // Bird
        bird = new Bird(boardWidth / 8,boardHeight / 2,34,24,birdImage);

        // Managers
        scoreManager = new ScoreManager();
        pipeManager = new PipeManager(boardWidth,64,512,topPipeImage,bottomPipeImage);

        difficulty = Difficulty.NORMAL;
        gameState = GameState.READY;

        pipeSpawnTimer =new Timer(2000,e -> pipeManager.createPipePair());
        gameLoopTimer =new Timer(1500 / 60,this);

        SwingUtilities.invokeLater(this::setDifficultyAndStart);
    }

    private Image loadImage(String path) {

        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("Image not found: " + path);
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    private void setDifficultyAndStart() {

        Difficulty selectedDifficulty = (Difficulty)JOptionPane.showInputDialog(this,"Select Difficulty","Difficulty",JOptionPane.QUESTION_MESSAGE,null,Difficulty.values(),Difficulty.NORMAL);

        if (selectedDifficulty == null) {
            System.exit(0);
        }

        difficulty = selectedDifficulty;
        pipeManager.setDifficulty(difficulty);
        scoreManager.setDifficulty(difficulty);

        startGame();
    }

    private void startGame() {

        bird.reset();

        pipeManager.reset();

        scoreManager.resetScore();
        pipeManager.createPipePair();
        gameState = GameState.RUNNING;

        pipeSpawnTimer.start();
        gameLoopTimer.start();

        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage,0,0,boardWidth,boardHeight,null);
        }

        bird.draw(g);

        pipeManager.draw(g);

        drawScore(g);
    }

    private void drawScore(Graphics g) {

        String score = String.valueOf(scoreManager.getScore());

        int digitWidth = 24;
        int digitHeight = 36;
        int gap = 4;

        int startX = (getWidth()- score.length()* (digitWidth + gap))/ 2;
        int y = 50;

        for (int i = 0; i < score.length(); i++) {
            int num =Character.getNumericValue(score.charAt(i));
            g.drawImage(digits[num],startX + i * (digitWidth + gap),y,digitWidth,digitHeight,null);
        }

        if (gameState == GameState.GAME_OVER
                && gameOverImg != null) {

            g.drawImage(gameOverImg,(getWidth() - 200) / 2,(getHeight() - 60) / 2,200,60,null);
        }

        // g.setColor(Color.WHITE);

        // g.setFont(
        //         new Font(
        //                 "Arial",
        //                 Font.PLAIN,
        //                 24
        //         )
        // );

        // g.drawString(
        //         "High: "
        //                 + scoreManager.getHighScore(),
        //         10,
        //         65
        // );

        // g.drawString(
        //         "Mode: "
        //                 + difficulty.getDisplayName(),
        //         10,
        //         95
        // );
    }

    private void updateGame() {

        bird.update(gravity);

        pipeManager.update();

        checkScore();
        checkCollision();
    }

    private void checkScore() {

        List<Pipe> pipes = pipeManager.getPipes();

        for (Pipe pipe : pipes) {
            boolean passed =!pipe.isPassed() && bird.getX()> pipe.getX()+ pipe.getWidth();
            if (passed) {
                pipe.setPassed(true);

                if (pipe.isScoringPipe()) {
                    scoreManager.registerPassedPipePair();
                }
            }
        }
    }

    private void checkCollision() {

        for (Pipe pipe :pipeManager.getPipes()) {
            if (bird.getBounds().intersects(pipe.getBounds())) {
                gameOver();
                return;
            }
        }

        if (bird.getY()+ bird.getHeight()> boardHeight) {
            gameOver();
        }
    }

    private void gameOver() {

        if (gameState == GameState.GAME_OVER) {
            return;
        }

        gameState = GameState.GAME_OVER;

        pipeSpawnTimer.stop();
        gameLoopTimer.stop();

        scoreManager.saveHighScoreIfNeeded();

        repaint();

        SwingUtilities.invokeLater(this::showGameOverDialog);
    }

    private void showGameOverDialog() {

        String message ="Game Over!\n"+ "Difficulty: "+ difficulty.getDisplayName()+ "\nScore: "+ scoreManager.getScore()+ "\nHigh Score: "+ scoreManager.getHighScore()+ "\n\nPlay Again?";

        int choice = JOptionPane.showConfirmDialog(this,message,"Round Finished",JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            setDifficultyAndStart();

        } else {

            System.exit(0);
        }
        else {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.RUNNING) {
            updateGame();

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()== KeyEvent.VK_SPACE){
            bird.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}