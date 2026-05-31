import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.Timer;

import java.util.*;

public class GamePanel extends JFrame implements KeyListener,ActionListener {
    private final int boardWidth;
    private final int boardHeight;
    private final Image bgImage;
    private final Bird bird;
    private final int gravity;
    private final PipeManager pipeManager;
    private final ScoreManager scoreManager;
    private final Timer gameLoopTimer;
    private final Timer pipeSpawnTimer;
    private GameState gameState;
    private Difficulty difficulty;
    private Image[] digit= new Image[10];
    private Image gameOverImg;


    public GamePanel(int boardWidth, int boardHeight){
        this.boardHeight=boardHeight;
        this.boardWidth=boardWidth;
        this.gravity=2; // this pulls back to the land
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(boardWidth,boardHeight));

        this.bgImage= loadImage("/main/asset/flappybirdbg.png");
        Image topPipeImage= loadImage("/main/asset/toppipe.png");
        Image bottomPipeImage= loadImage("/main/asset/bottompipe.png");
        Image birdImage= loadImage("/main/asset/bird.png");

        //bird
        int birdX= boardWidth/8;
        int birdY= boardHeight/2;
        this.bird= new Bird(birdX, birdY, 34, 24, birdImage);

        this.scoreManager= new ScoreManager();
        this.pipeManager= new PipeManager(boardWidth, 64, 512, topPipeImage, bottomPipeImage);

        this.gameState= GameState.READY;
        this.difficulty= Difficulty.NORMAL;

        this.pipeSpawnTimer= new Timer(1500, event -> pipeManager.createPipePair());
        this.gameLoopTimer= new Timer(1000/60, this);

        SwingUtilities.invokeLater(this::setDifficultyAndStart);
        
        // score
        for(int i=0; i<=9; i++){
            digit[i]= loadImage("/main/asset/"+ i+ ".png");
        }
        gameOverImg= loadImage("/main/asset/gameover.png");
    }

    //load the image
    private Image loadImage(String filename) {
        return new ImageIcon(getClass().getResource(filename)).getImage();
    }

    private void setDifficultyAndStart(){
        Difficulty selectDifficulty= JOptionPane.showInputDialog(this, "Select Game Difficulty", "Difficulty", JOptionPane.QUESTION_MESSAGE, null, Difficulty.values(), Difficulty.NORMAL);
        if(selectDifficulty == null){
            System.exit(0);
        }
        difficulty= selectDifficulty;
        pipeManager.setDifficulty(difficulty);
        scoreManager.setDifficulty(difficulty);
        startGame();
    }

    private void startGame() {
        // TODO Auto-generated method stub
        bird.reset();
        pipeManager.reset();
        scoreManager.resetScore();
        gameState= GameState.RUNNING;
        pipeSpawnTimer.start();
        gameLoopTimer.start();
        requestFocusInWindow();
    }
    protected void paintComponent(Graphics g){
        super.paintComponents(g);
        draw(g);
    }
    //place all graphical components.
    private void draw(Graphics g){
        g.drawImage(bgImage, 0, 0, boardWidth ,boardHeight, null);
        bird.draw(g);
        pipeManager.draw(g);
        drawScore(g);
    }

    //scoring
    private void drawScore(Graphics g){
        String score= String.valueOf(scoreManager.getScore());

        int dgtWidth= 24;
        int dgtHeight= 36;
        int dgtGap= 4;

        int scrX= (getWidth()- score.length()*(dgtWidth+dgtGap))/2; 
        int scrY=50;

        for(int i=0; i<score.length(); i++){
            int num= Character.getNumericValue(score.charAt(i));
            int x= scrX+ i*(dgtWidth+dgtGap);

            g.drawImage(digit[num],x,scrY, dgtWidth, dgtHeight,null);
        }
        //game over image print
        if(gameState==gameState.GAME_OVER){
            int imgWidth= 200;
            int imgHeight= 60;
            int imgX= (getWidth()-imgWidth)/2;
            int imgY= (getHeight()-imgHeight)/2;

            g.drawImage(gameOverImg, imgX, imgY, imgWidth, imgHeight, null);
        }

        //hence no asset found for highest score & Mode
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        g.drawString("High: "+scoreManager.getHighScore(), 10, 65);
        g.drawString("Mode: "+difficulty.getDisplayName(), 10, 95);
    }

    private void updateGame(){
        bird.update(gravity);
        pipeManager.update();
        checkScore();
        checkCollisions();
    }

    private void checkCollisions() {
        for(Pipe pipe: pipeManager.getPipes()){
            boolean passed = !pipe.isPassed() && bird.getX()>pipe.getX() +pipe.getWidth();
            if(passed){
                pipe.setPassed(true);
                if(pipe.isScoringPipe()){
                    scoreManager.registerPassedPipePair();
                }
            }
        }
    }

    private void checkScore() {
        for(Pipe pipe: pipeManager.getPipes()){
            if(bird.getBounds().intersects(pipe.getBounds())){
                theEnd();
                return;
            }
            if(bird.getY()+bird.getHeight()>boardHeight){
                theEnd();
            }
        }
    }

    private void theEnd() {
        // TODO Auto-generated method stub
        if(gameState==gameState.GAME_OVER){
            return;
        }
        gameState= gameState.GAME_OVER;
        pipeSpawnTimer.stop();
        gameLoopTimer.stop();
        scoreManager.saveHighScoreIfNeeded();
        repaint();
        SwingUtilities.invokeLater(this::showGameOverDialog);
    }

    private void showGameOverDialog(){
        String message= "Game over !!!\nDifficulty:"+difficulty.getDisplayName()+"\nScore:"+scoreManager.getScore()+"\nHigh Score:" +scoreManager.getHighScore()+"\n Play Again ??";
        int choice= JOptionPane.showConfirmDialog(this, message, "Round Finished", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            setDifficultyAndStart();
        }
        else System.exit(0);
    }

    //handle timer event
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameState== GameState.RUNNING){
            updateGame();
            repaint();
        }
    }
    //handle key press
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){// use SPACE-BAR for control
            bird.jump();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//we dont need that till now
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //we dont need that till now;
    }
}