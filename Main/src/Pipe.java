import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Pipe {

    private final Image image;
    private final int width;
    private final int height;

    private int x;
    private int y;

    private boolean passed;
    private final boolean scoringPipe;

    public Pipe(int x,int y,int width,int height,Image image,boolean scoringPipe) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;

        this.passed = false;
        this.scoringPipe = scoringPipe;
    }

    public void update(int velocityX) {
        x += velocityX;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isScoringPipe() {
        return scoringPipe;
    }
}