import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Bird {

    private final int startX;
    private final int startY;
    private final int width;
    private final int height;
    private final Image image;

    private int x;
    private int y;
    private int velocityY;

    public Bird(int startX, int startY, int width, int height, Image image) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.image = image;

        reset();
    }

    public void reset() {
        x = startX;
        y = startY;
        velocityY = 0;
    }

    public void jump() {
        velocityY = -9;
    }

    public void update(int gravity) {
        velocityY += gravity;
        y += velocityY;

        y = Math.max(y, 0);
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
}