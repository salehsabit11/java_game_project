import java.awt.*;

public class Pipe {
    private final Image img;
    private final int width;
    private final int height;
    private int x;
    private int y;
    private boolean passed;
    private final boolean scoringPipe;

    public Pipe(int x, int y, int width, int height, Image img, boolean scoringPipe){
        this.x=x;
        this.y=y;
        this.width= width;
        this.height= height;
        this.img= img;
        this.passed= false;
        this.scoringPipe= scoringPipe;
    }

    public void update(int velX){
        x+= velX;
    }

    public void draw(Graphics g){
        g.drawImage(img, x, y, width, height, null);
    }
    
    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getWidth(){ return width;}
    public int getHeight(){ return height;}
    public boolean isPassed(){ return passed;}
    public boolean scoringPipe(){ return scoringPipe;}
    public Rectangle getBounds(){ return new Rectangle(x, y, width, height);}

    public void setPassed(boolean passed){
        this.passed= passed;
    }
}
