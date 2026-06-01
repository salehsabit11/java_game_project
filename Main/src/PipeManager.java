import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PipeManager {

    private final int boardWidth;
    private final int pipeWidth;
    private final int pipeHeight;

    private final Image topPipeImage;
    private final Image bottomPipeImage;

    private final List<Pipe> pipes;
    private final Random random;

    private Difficulty difficulty;

    public PipeManager(
            int boardWidth,
            int pipeWidth,
            int pipeHeight,
            Image topPipeImage,
            Image bottomPipeImage) {

        this.boardWidth = boardWidth;
        this.pipeWidth = pipeWidth;
        this.pipeHeight = pipeHeight;

        this.topPipeImage = topPipeImage;
        this.bottomPipeImage = bottomPipeImage;

        this.pipes = new ArrayList<>();
        this.random = new Random();

        this.difficulty = Difficulty.NORMAL;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void reset() {
        pipes.clear();
    }

    public void createPipePair() {

        int openingSpace =
                difficulty.getOpeningSpace();

        int randomPipeY =
                -pipeHeight / 4
                        - random.nextInt(pipeHeight / 2);

        Pipe topPipe =
                new Pipe(
                        boardWidth,
                        randomPipeY,
                        pipeWidth,
                        pipeHeight,
                        topPipeImage,
                        true
                );

        Pipe bottomPipe =
                new Pipe(
                        boardWidth,
                        randomPipeY + pipeHeight + openingSpace,
                        pipeWidth,
                        pipeHeight,
                        bottomPipeImage,
                        false
                );

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    public void update() {

        for (Pipe pipe : pipes) {

            pipe.update(
                    difficulty.getPipeVelocityX()
            );
        }

        removeOffScreenPipes();
    }

    private void removeOffScreenPipes() {

        Iterator<Pipe> iterator =
                pipes.iterator();

        while (iterator.hasNext()) {

            Pipe pipe = iterator.next();

            if (pipe.getX() + pipe.getWidth() < 0) {

                iterator.remove();
            }
        }
    }

    public void draw(Graphics g) {

        for (Pipe pipe : pipes) {

            pipe.draw(g);
        }
    }

    public List<Pipe> getPipes() {
        return pipes;
    }
}