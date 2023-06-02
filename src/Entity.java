import java.awt.*;

public class Entity {
    public Point[] points;
    public int[] distance;
    public GamePanel gp;
    public double x;
    public double y;
    public double z;
    int tileX = 0; int tileZ = 0;


    public Entity(GamePanel gp, double x, double y, double z){
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileX = (int) (x / gp.TILE_SIZE);
        this.tileZ = (int) (x / gp.TILE_SIZE);
    }



}
